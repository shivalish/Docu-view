
function TableHeaders({ sortFiles, currentSort }) {

    const sortButton = (key) => {
        return (
            <button
                onClick={() => sortFiles(key)}
                className="transform transition-transform duration-200 ml-2.5"
                style={{ transform: currentSort.key === key && currentSort.direction === 'descending' ? 'rotate(180deg)' : '' }}
            >
                ^
            </button>
        );
    };

    return (
        <div className="flex justify-center mb-2">
            <div className='w-11/12 flex border-b border-gray-400'>
                <div className="h-5 w-5 mr-4 ml-4"></div>
                <div className="w-1/4 text-sm px-2 font-semibold flex items-center">
                    Filename {sortButton('fileName')}
                </div>
                <div className="w-1/4 text-sm px-2 font-semibold flex items-center">
                    Customer {sortButton('customer')}
                </div>
                <div className="w-1/4 text-sm px-2 font-semibold flex items-center">
                    Upload Date {sortButton('uploadDate')}
                </div>
                <div className="w-1/4 text-sm px-2 font-semibold flex items-center">
                    File Size {sortButton('fileSizeMB')}
                </div>
            </div>
        </div>
    )

}

export default TableHeaders