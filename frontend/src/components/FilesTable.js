import React, { useState } from 'react'
import FileRow from './FileRow'
import TableHeaders from './TableHeaders'

function FilesTable() {

    /*
    When we receive files from the server we put them in this array
    We can 'sort' the file table by sorting this array, since the table maps row 
    elements in order based on this array

    uploadDate is current ms since epoch since that's a pretty standard way to store
    dates, can change once backend team tells us how they will be sending us them
    */
    const [files, setFiles] = useState([
        {
            fileName: "testFile1",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 50,
            attachmentID: 50,
        }, {
            fileName: "testFile2",
            customer: "Adam",
            uploadDate: 1697724214692,
            fileSizeMB: 95,
            attachmentID: 20,
        }, {
            fileName: "testFile3",
            customer: "Shiyu",
            uploadDate: 1697718214692,
            fileSizeMB: 4000,
            attachmentID: 35,
        }, {
            fileName: "testFile4",
            customer: "Wenhan",
            uploadDate: 1697768252692,
            fileSizeMB: 500,
            attachmentID: 69430,
        }, {
            fileName: "eepyFile",
            customer: "EvilJake",
            uploadDate: 1697268112692,
            fileSizeMB: 100,
            attachmentID: 90101,
        }, {
            fileName: "stinkyFile",
            customer: "EvilAdam",
            uploadDate: 1197268253332,
            fileSizeMB: 1560,
            attachmentID: 8000,
        }, {
            fileName: "bestFile",
            customer: "EvilShiyu",
            uploadDate: 1297268253332,
            fileSizeMB: 150,
            attachmentID: 1,
        }, {
            fileName: "largeFile",
            customer: "EvilWenhan",
            uploadDate: 1697168252692,
            fileSizeMB: 2142,
            attachmentID: 603,
        }]
    )

    const [selectedFiles, setSelectedFiles] = useState([]);

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

    const handleFileSelection = (attachmentID) => {
        setSelectedFiles((prevSelectedFiles) => {
            if (prevSelectedFiles.includes(attachmentID)) {
                return prevSelectedFiles.filter(id => id !== attachmentID);
            } else {
                return [...prevSelectedFiles, attachmentID];
            }
        });
    };

    const selectionFooter = () => {
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
            <div className="flex justify-between items-center mb-4">
                <div className="text-lg font-bold text-iso-blue-grey">
                    Results...
                </div>
                <div className="space-x-2">
                    <button className="bg-iso-blue-grey text-white px-4 py-2 rounded">View</button>
                    <button className="bg-iso-blue-grey text-white px-4 py-2 rounded">Download</button>
                </div>
            </div>

            <TableHeaders sortFiles={sortFiles} currentSort={currentSort} />

            <div className='flex justify-center'>
                <div className='w-11/12 border border-gray-400'>
                    {files.map((fileData, index) =>
                        <div key={fileData.attachmentID} className={index % 2 ? '' : 'bg-iso-white'}>
                            <FileRow
                                fileName={fileData.fileName}
                                customer={fileData.customer}
                                uploadDate={fileData.uploadDate}
                                fileSizeMb={fileData.fileSizeMB}
                                attachmentID={fileData.attachmentID}
                                isSelected={selectedFiles.includes(fileData.attachmentID)}
                                onFileSelection={handleFileSelection}
                            />
                        </div>
                    )}
                    {selectionFooter()}
                </div>

            </div>
        </div>
    )

}

export default FilesTable
