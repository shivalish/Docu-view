
function TableHeaders() {

    return (
        <div className="flex justify-center mb-2">
            <div className='w-11/12 flex border-b border-gray-400'>
                <div className="h-5 w-5 mr-4 ml-4"></div>
                <div className="w-1/4 text-sm px-2 font-semibold">Filename</div>
                <div className="w-1/4 text-sm px-2 font-semibold">Customer</div>
                <div className="w-1/4 text-sm px-2 font-semibold">Upload Date</div>
                <div className="w-1/4 text-sm px-2 font-semibold">File Size</div>
            </div>
        </div>

    )

}

export default TableHeaders