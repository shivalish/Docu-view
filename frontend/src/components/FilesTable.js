import React, { useState, useContext, useEffect } from 'react'
import FileRow from './FileRow'
import TableHeaders from './TableHeaders'
import Button from '../atoms/Button'
import { FetchContext } from "./TableContext.jsx";
import DummyData from '../atoms/DummyData.js';
import Popup from '../atoms/Popup.jsx';
import { Tab } from '@headlessui/react';

function FilesTable() {

    //popup states
    const [openPreview, setOpenPreview] = useState(false);
    const [openDownload, setOpenDownload] = useState(false);

    const {val} = useContext(FetchContext);

    //this function executes on every element of the DummyData array
    const filterFunc = (e) => {
        //temporary filter function
        let fitsCriteria = true;
        for (const key in val){
            if(key === "file_name" || key === "customer_name"){
                if(val[key].length === 0) continue;
                fitsCriteria = fitsCriteria && val[key].filter(name => (e[key].includes(name))).length > 0;
            }
        }
        return fitsCriteria;
    }
    useEffect(()=>{
        setFiles(DummyData.filter(filterFunc));
    },[val])
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
            return;
        }
        setFiles((oldFiles) => {
            let newFiles = [...oldFiles];
            newFiles.sort((fileA, fileB) => {
                if (typeof fileA[key] === "string") {
                    return byAscending ? fileA[key].localeCompare(fileB[key]) : fileB[key].localeCompare(fileA[key]);
                } else {
                    return byAscending ? fileA[key] - fileB[key] : fileB[key] - fileA[key];
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

    return (
        <div className='bg-iso-grey h-full w-full p-4'>
            
            <Popup
            onOpen={openPreview}
            onClose={()=>{setOpenPreview(false)}}>
                <div className="flex flex-col">
                    <Tab.Group>
                        <Tab.List className="grid grid-cols-5 ">{selectedFiles.map(e => (
                            <Tab className="tab">{DummyData.find(f => f.attachmentID === e).file_name}</Tab>
                        )
                        
                        )}</Tab.List>

                        <Tab.Panels>
                            {selectedFiles.map(e => (
                                <Tab.Panel className="flex-1 tab-body !p-0">
                                    <div className="flex flex-row w-full h-full">
                                        <div className="flex flex-col gap-1 h-60 w-1/3">
                                            {Object.entries(DummyData.find(f => f.attachmentID === e)).map(kv => {
                                                const key = kv[0].replace("_", ' ').replace(/([a-z])([A-Z])/g, '$1 $2');
                                                const val = kv[1];
                                                //replace dummydata with actual data
                                                //kv is a key/value pair of some object in dummydata with attachmentID specified by selected file
                                                return (
                                                    <div className="block font-bold text-xs first-letter:capitalize">
                                                        {`${key}: ${val}`}
                                                    </div>
                                                )
                                            })
                                            
                                            }

                                        </div>

                                        <div className="flex h-60 w-2/3">
                                            {Object.entries(DummyData.find(f => f.attachmentID === e)).map(async kv => {
                                                //fetch png
                                                // const source = (await fetch("")).data.json();

                                                // return (
                                                //     <img src={source}/>
                                                // )
                                            })
                                            
                                            }
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
                    <Button className="bg-iso-blue-grey-100 text-white px-4 py-2 rounded" OnClick={()=>setOpenPreview(true)}>View</Button>
                    <Button className="bg-iso-blue-grey-100 text-white px-4 py-2 rounded">Download</Button>
                </div>
            </div>

            <TableHeaders sortFiles={sortFiles} currentSort={currentSort} />
            <div className='flex justify-center'>
                <div className='w-11/12 border border-gray-400'>
                    {files
                        .slice((filePage - 1) * filesPerPage, filePage * filesPerPage)
                        .map((fileData, index) => (
                            <div key={fileData.attachmentID} className={index % 2 ? '' : 'bg-iso-white'}>
                                <FileRow
                                    fileName={fileData.file_name}
                                    customer={fileData.customer_name}
                                    uploadDate={fileData.uploadDate}
                                    fileSizeMb={fileData.fileSizeMB}
                                    attachmentID={fileData.attachmentID}
                                    isSelected={selectedFiles.includes(fileData.attachmentID)}
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
