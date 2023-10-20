import React from "react";
import classNames from "classnames";

export default function Button({ children, width, height, OnClick }) {
  //TODO 5: create a hover effect and animate the size of the button using only tailwind css
  return (
    <button
      type="button"
      onClick={OnClick}
      className={classNames(
        "bg-iso-blue-grey-100 font-bold text-white rounded-md transition-all duration-300 ease-in-out hover:scale-110 hover:bg-iso-blue-grey-200",
        width,
        height
      )}
    >
      {children}
    </button>
  );
}
