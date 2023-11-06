import React from "react";

const SelectionTag = ({ value, onDelete }) => {
  return (
    <div className="flex justify-between items-center bg-iso-blue-grey-200 text-white rounded-full px-4 py-1 m-3 max-w-xs">
      <span className="text-xs sm:text-sm font-medium mr-2 overflow-hidden break-words text-left flex-1">
        {value}
      </span>
      <button
        onClick={() => onDelete(value)}
        className="text-xs bg-red-500 hover:bg-red-600 text-white rounded-full p-1 focus:outline-none transition duration-300 ease-in-out"
        aria-label="Delete"
      >
        &times;
      </button>
    </div>
  );
};

export default SelectionTag;
