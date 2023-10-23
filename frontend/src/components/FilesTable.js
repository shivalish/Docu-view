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
        }, {
            fileName: "testFile1",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 650,
            attachmentID: 5015,
        }, {
            fileName: "testFile2",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 780,
            attachmentID: 5014,
        }, {
            fileName: "testFile3",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 100,
            attachmentID: 5013,
        }, {
            fileName: "testFile4",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 56,
            attachmentID: 5012,
        }, {
            fileName: "testFile5",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 75,
            attachmentID: 5011,
        }, {
            fileName: "testFile6",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 90,
            attachmentID: 5010,
        }, {
            fileName: "testFile7",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 60,
            attachmentID: 5009,
        }, {
            fileName: "testFile8",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 5,
            attachmentID: 5008,
        }, {
            fileName: "testFile9",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 2,
            attachmentID: 5007,
        }, {
            fileName: "testFile10",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 90,
            attachmentID: 5006,
        }, {
            fileName: "testFile11",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 5040,
            attachmentID: 5005,
        }, {
            fileName: "testFile12",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 5220,
            attachmentID: 5004,
        }, {
            fileName: "testFile13",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 550,
            attachmentID: 5003,
        }, {
            fileName: "testFile14",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 70,
            attachmentID: 5002,
        }, {
            fileName: "testFile15",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 60,
            attachmentID: 83,
        }, {
            fileName: "testFile25",
            customer: "Jake",
            uploadDate: 1697768214692,
            fileSizeMB: 9000,
            attachmentID: 801,
        }, {
            fileName: "testFile45",
            customer: "Jake2",
            uploadDate: 1697768214692,
            fileSizeMB: 60,
            attachmentID: 87,
        }, {
            fileName: "testFile1006",
            customer: "Jake3",
            uploadDate: 1697768214692,
            fileSizeMB: 600,
            attachmentID: 89,
        }]
    )

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
            <div className="flex justify-between items-center mb-4">
                <div className="text-lg font-bold text-iso-blue-grey">
                    Results...
                </div>
                <div className="space-x-2">
                    <button className="bg-iso-blue-grey-100 text-white px-4 py-2 rounded">View</button>
                    <button className="bg-iso-blue-grey-100 text-white px-4 py-2 rounded">Download</button>
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
                                    fileName={fileData.fileName}
                                    customer={fileData.customer}
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
