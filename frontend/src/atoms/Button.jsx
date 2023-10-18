import React from "react";
import classNames from "classnames";

export default function Button({ children, width, height, OnClick }) {
  //TODO 5: create a hover effect and animate the size of the button using only tailwind css
  return (
    <button
      type="button"
      onClick={OnClick}
      className={classNames(
        "bg-iso-blue-grey font-bold text-white rounded-md",
        width,
        height
      )}
    >
      {children}
    </button>
  );
}
