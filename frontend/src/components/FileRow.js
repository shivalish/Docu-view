import React from 'react';

function formatDate(milliseconds) {
    const date = new Date(milliseconds);
    const options = {
        month: '2-digit',
        day: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    };
    return new Intl.DateTimeFormat('en-US', options).format(date);
}

function formatFileSize(mbSize) {
    if (mbSize < 1000) {
        return `${mbSize} MB`;
    } else {
        const gbSize = mbSize / 1000;
        return `${gbSize.toFixed(2)} GB`;
    }
}


const FileRow = ({ fileName, customer, uploadDate, fileSizeMb, attachmentID, isSelected, onFileSelection }) => {
    return (
        <div className="flex w-full border-b py-2 items-center">
            <input
                type="checkbox"
                className="form-checkbox h-5 w-5 mr-4 ml-4"
                checked={isSelected}
                onChange={() => onFileSelection(attachmentID)}
            />
            <div className="w-1/4 truncate text-sm px-2">{fileName}</div>
            <div className="w-1/4 truncate text-sm px-2">{customer}</div>
            <div className="w-1/4 truncate text-sm px-2">{formatDate(uploadDate)}</div>
            <div className="w-1/4 truncate text-sm px-2">{formatFileSize(fileSizeMb)}</div>
        </div>
    );
}


export default FileRow;
