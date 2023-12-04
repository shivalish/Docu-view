import React, { useState, useEffect } from 'react';

function formatDate(uDate) {
    if (!uDate) return uDate
    if (typeof (uDate) === 'string') {
        uDate = new Date(uDate)?.getTime()
    }

    const date = new Date(uDate);
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

const FileRow = ({ fileName, customer, uploadDate, description, fileSizeMb, attachmentID, isSelected, onFileSelection }) => {
    const [descriptionTip, setDescriptionTip] = useState(false);
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
            <div className="w-1/4"
                style={{ overflowX: 'hidden' }}
                onMouseEnter={() => setDescriptionTip(true)}
                onMouseLeave={() => setDescriptionTip(false)}>
                <div
                    className="w-full text-sm px-2 relative truncate"
                    style={{opacity: descriptionTip ? '0.6' : '1'}}
                    >
                    {description}
                </div>
                {descriptionTip && (
                    <p className="absolute w-56 border border-solid bg-white text-sm text-black p-2 z-20"
                        style={{ whiteSpace: 'normal', overflow: 'visible', marginTop: '-0.2vh', pointerEvents: 'none' }}>
                        {description}
                    </p>
                )}
            </div>

        </div>
    );
}


export default FileRow;
