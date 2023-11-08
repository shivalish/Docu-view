import React from "react";
import { XCircleIcon } from "@heroicons/react/24/solid";

const SelectionTag = ({ value, onDelete }) => {
  return (
    <div className="flex w-full justify-between items-center bg-iso-blue-grey-300 text-white rounded-md px-4 py-2 my-3 max-w-xs">
      <span className="text-xs sm:text-sm font-medium mr-2 overflow-hidden break-words text-left flex-1 line-clamp-1">
        {value}
      </span>
      <XCircleIcon 
      className="fill-red-500 hover:fill-red-600 text-white rounded-full w-5 h-5 focus:outline-none transition duration-300 ease-in-out"
      aria-label="Delete"
      onClick={() => onDelete(value)} />
    </div>
  );
};

export default SelectionTag;
