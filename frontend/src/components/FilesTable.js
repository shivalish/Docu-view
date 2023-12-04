import React, { useState, useContext, useEffect } from 'react'
import FileRow from './FileRow'
import TableHeaders from './TableHeaders'
import Button from '../atoms/Button'
import { FetchContext, SubmitContext } from "./TableContext.jsx";
import DummyData from '../atoms/DummyData.js';
import Popup from '../atoms/Popup.jsx';
import { Tab } from '@headlessui/react';
import ImagePreview from '../atoms/ImagePreview.js';
import axios from 'axios'
function FilesTable() {
    //popup states
    const [openPreview, setOpenPreview] = useState(false);
    const {val} = useContext(FetchContext);
    const {update} = useContext(SubmitContext);

    function parseParams(params) {
        const keys = Object.keys(params)
        let options = ''
        keys.forEach((key) => {
            const isParamTypeObject = typeof params[key] === 'object'
            const isParamTypeArray = isParamTypeObject && params[key].length >= 0
            if (!isParamTypeObject) {
                options += `${key}=${params[key]}&`
            }
            if (isParamTypeObject && isParamTypeArray) {
                params[key].forEach((element) => {
                    options += `${key}=${element}&`
                })
            }
        })
        return options ? options.slice(0, -1) : options
    }

    const x = async () => {
        const res = await axios.get('http://localhost:8080/api/v1/database', {
            params: {
                ...val
            },
            paramsSerializer: parseParams
        });
        // console.log('THIS IS... ', res.data);
        return res.data;
    }

    const fetchData = async () => {
        setFiles([]);
        const resultingFiles = await x();
        setFiles(resultingFiles);
    }

    useEffect(()=>{
        fetchData();
        setSelectedFiles([]);
    }, [update])
    /*
    When we receive files from the server we put them in this array
    We can 'sort' the file table by sorting this array, since the table maps row 
    elements in order based on this array
    uploadDate is current ms since epoch since that's a pretty standard way to store
    dates, can change once backend team tells us how they will be sending us them
    */
    const [files, setFiles] = useState(DummyData);
    const [selectedFiles, setSelectedFiles] = useState([]);
    const [filePage, setFilePage] = useState(1);
    const filesPerPage = 12;
    const totalFiles = files.length;
    const [currentSort, setCurrentSort] = useState({ key: '', direction: 'ascending' });
    const sortFiles = (key) => {
        const byAscending = currentSort.key === key ? currentSort.direction === 'descending' : true;
        setCurrentSort({ key, direction: byAscending ? 'ascending' : 'descending' });
        if (!files || files.length === 0 || !Object.keys(files[0]).includes(key)) {
            console.log("NO FILES OR INVALID KEY")
            return;
        }
        setFiles((oldFiles) => {
            let newFiles = [...oldFiles];
            newFiles.sort((fileA, fileB) => {
                if (typeof fileA[key] === "string") {
                    return byAscending ? fileA?.[key]?.localeCompare(fileB?.[key]) : fileB?.[key]?.localeCompare(fileA?.[key]);
                } else {
                    return byAscending ? fileA?.[key] - fileB?.[key] : fileB?.[key] - fileA?.[key];
                }
            });
            return newFiles;
        });
    }
    const handlePageChange = (direction) => {
        setFilePage((prevPage) => {
            if (direction === 'prev' && prevPage > 1) {
                return prevPage - 1;
            } else if (direction === 'next' && prevPage < Math.ceil(totalFiles / filesPerPage)) {
                return prevPage + 1;
            }
            return prevPage;
        });
    };
    const handleFileSelection = (attachmentID) => {
        setSelectedFiles((prevSelectedFiles) => {
            if (prevSelectedFiles.includes(attachmentID)) {
                return prevSelectedFiles.filter(id => id !== attachmentID);
            } else {
                return [...prevSelectedFiles, attachmentID];
            }
        });
    };
    const filePageFooter = () => {
        const startFile = (filePage - 1) * filesPerPage + 1;
        const endFile = Math.min(filePage * filesPerPage, totalFiles);
        return (
            <div className="flex justify-center items-center mt-4 p-2 bg-gray-100 rounded">
                <button
                    onClick={() => handlePageChange('prev')}
                    className="text-blue-500 text-xl disabled:opacity-50 mx-2"
                    disabled={filePage === 1}
                >
                    &lt;
                </button>
                <p className="text-gray-600 text-sm mx-2">
                    Viewing files {startFile} - {endFile} of {totalFiles}
                </p>
                <button
                    onClick={() => handlePageChange('next')}
                    className="text-blue-500 text-xl disabled:opacity-50 mx-2"
                    disabled={filePage === Math.ceil(totalFiles / filesPerPage)}
                >
                    &gt;
                </button>
            </div>
        );
    };
    const fileCountFooter = () => {
        if (selectedFiles.length > 0) {
            return (
                <p className="italic text-sm text-gray-600 ml-3">
                    {selectedFiles.length} file{selectedFiles.length > 1 ? 's' : ''} selected
                </p>
            );
        }
    }

    const downloadFiles = async () => {
        if (selectedFiles.length === 0) return;
        console.log(`downloading: ${selectedFiles}`)
        const res = await axios.get('http://localhost:8080/api/v1/fileshare/download/zipFiles', {
            params: {
                fileIds: selectedFiles.join(',')
            },
            responseType: 'blob'
        }).then((response) => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'file.zip'); // or any other filename you want
            document.body.appendChild(link);
            link.click();
        })
            .catch((error) => console.error(error));;
    }

    // For now this fetches a preview for the FIRST image selected only, and stores it's binary data in localstorage
    function getPreview() {
        if (selectedFiles.length === 0) return;
        let targetFileID = selectedFiles[0];
        if (!Number.isInteger(targetFileID)) return;
        console.log(`getting preview for: ${targetFileID}`)

        const url = `http://localhost:8080/api/v1/fileshare/preview/${targetFileID}`;

        axios.get(url, { responseType: 'blob' })
            .then(response => {
                const reader = new FileReader();
                reader.readAsDataURL(response.data);
                reader.onloadend = function () {
                    const base64data = reader.result;
                    localStorage.setItem(`filePreview${targetFileID}`, base64data);
                    console.log('successfully stored preview')
                }
            })
            .catch(error => {
                console.error('Error fetching the image:', error);
            });
    }

    return (
        <div className='bg-iso-grey h-full w-full p-4'>

            <Popup
            onOpen={openPreview && selectedFiles?.length > 0}
            onClose={()=>{setOpenPreview(false)}}>
                <div className="flex flex-col">
                    <Tab.Group>
                        <Tab.List className="grid grid-cols-5">{selectedFiles?.map(e => (
                            <Tab className="tab !w-auto !h-auto truncate">{files?.find(f => f.attachmentId === e)?.attachmentFileName}</Tab>
                        )

                        )}</Tab.List>
                        <Tab.Panels>
                            {selectedFiles?.map(e => (
                                <Tab.Panel className="flex-1 tab-body !p-0">
                                    <div className="flex flex-row w-full h-full">
                                        <div className="flex flex-col gap-1 h-60 w-1/3 p-1">
                                            <div className="flex flex-col gap-1 h-full w-full overflow-y-auto">
                                            {files?.find(f => f.attachmentId === e)?.length > 0 && (Object.entries(files?.find(f => f.attachmentId === e))?.map(kv => {
                                                    const key = kv[0].replace(/([a-z])([A-Z])/g, '$1 $2');
                                                    const val = kv[1];
                                                    return (
                                                        <div className="block font-bold text-xs first-letter:capitalize">
                                                            {`${key}:`}
                                                            <br />
                                                            {`${val}`}
                                                        </div>
                                                    )
                                                }))
                                            }
                                            </div>

                                            <Button width="w-32" height="h-10" onClick={downloadFiles}> Download </Button>

                                        </div>

                                        <div className="flex h-60 w-2/3 p-1">
                                            <ImagePreview fileId={e} />
                                        </div>
                                    </div>
                                </Tab.Panel>
                            ))}
                        </Tab.Panels>
                    </Tab.Group>
                </div>
            </Popup>
            
            <div className="flex justify-between items-center mb-4">
                <div className="text-lg font-bold text-iso-blue-grey">
                    Results...
                </div>
                <div className="space-x-2">
                    <Button
                        className="bg-iso-blue-grey-100 text-white px-4 py-2 rounded"
                        OnClick={() => { getPreview(); setOpenPreview(true) }}
                    >View</Button>
                    <Button
                        className="bg-iso-blue-grey-100 text-white px-4 py-2 rounded"
                        OnClick={downloadFiles}
                    >Download</Button>
                </div>
            </div>
            <TableHeaders sortFiles={sortFiles} currentSort={currentSort} />
            <div className='flex justify-center'>
                <div className='w-11/12 border border-gray-400'>
                    {files
                        ?.slice((filePage - 1) * filesPerPage, filePage * filesPerPage)
                        ?.map((fileData, index) => (
                            <div key={fileData?.attachmentID} className={index % 2 ? '' : 'bg-iso-white'}>
                                <FileRow
                                    fileName={fileData.attachmentFileName}
                                    customer={fileData.customerName}
                                    uploadDate={fileData.createDate}
                                    description={fileData.typeDescription}
                                    attachmentID={fileData.attachmentId}
                                    isSelected={selectedFiles.includes(fileData.attachmentId)}
                                    onFileSelection={handleFileSelection}
                                />
                            </div>
                        ))
                    }
                </div>
            </div>
            {filePageFooter()}
            {fileCountFooter()}
        </div>
    );
}
export default FilesTable