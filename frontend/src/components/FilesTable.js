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
            fileSizeMB: 50
        }, {
            fileName: "testFile2",
            customer: "Adam",
            uploadDate: 1697724214692,
            fileSizeMB: 95
        }, {
            fileName: "testFile3",
            customer: "Shiyu",
            uploadDate: 1697718214692,
            fileSizeMB: 4000
        }, {
            fileName: "testFile4",
            customer: "Wenhan",
            uploadDate: 1697768252692,
            fileSizeMB: 500
        }, {
            fileName: "eepyFile",
            customer: "EvilJake",
            uploadDate: 1697268112692,
            fileSizeMB: 100
        }, {
            fileName: "stinkyFile",
            customer: "EvilAdam",
            uploadDate: 1197268253332,
            fileSizeMB: 1560
        }, {
            fileName: "bestFile",
            customer: "EvilShiyu",
            uploadDate: 1297268253332,
            fileSizeMB: 150
        }, {
            fileName: "largeFile",
            customer: "EvilWenhan",
            uploadDate: 1697168252692,
            fileSizeMB: 2142
        }]
    )

    /* key should be the key in the object files you want to sort by as a string ('fileName', 'customer', etc) and byAscending is boolean for sort order */
    const sortFiles = (key, byAscending) => {
        if (!files || files.length === 0 || !Object.keys(files[0]).includes(key)) {
            return;
        }
        setFiles((oldFiles) => {
            let newFiles = [...oldFiles];
            newFiles.sort((fileA, fileB) => {
                if (typeof fileA[key] === "string") {
                    return byAscending ?
                        fileA[key].localeCompare(fileB[key])
                        :
                        fileB[key].localeCompare(fileA[key]);
                } else {
                    return byAscending ?
                        fileA[key] - fileB[key]
                        :
                        fileB[key] - fileA[key];
                }
            })
            return newFiles;
        })
    }


    return (
        <div className='bg-iso-grey h-full w-full p-4'>
            <div className="flex justify-between items-center mb-4">
                <div className="text-lg font-bold text-iso-blue-grey">
                    Results...
                </div>
                <div className="space-x-2">
                    <button className="bg-iso-blue-grey text-white px-4 py-2 rounded">Sort By</button>
                    <button className="bg-iso-blue-grey text-white px-4 py-2 rounded">View</button>
                    <button className="bg-iso-blue-grey text-white px-4 py-2 rounded">Download</button>
                </div>
            </div>

            <TableHeaders />

            <div className='flex justify-center'>
                <div className='w-11/12 border border-gray-400'>
                    {files.map((fileData, index) =>
                        <div className={index % 2 ? '' : 'bg-iso-white'}>
                            <FileRow fileName={fileData.fileName} customer={fileData.customer} uploadDate={fileData.uploadDate} fileSizeMb={fileData.fileSizeMB} />
                        </div>
                    )}
                </div>
            </div>
        </div>
    )

}

export default FilesTable
