import React from "react";
import { Menu } from "@headlessui/react";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/solid";

function FiltersColumn() {
  return (
    <div className="bg-iso-blue h-full w-full">
      <Menu>
        <Menu.Button>
          <div className="flex flex-wrap w-full h-24 rounded-md text-left items-center text-iso-white justify-right">
            <span>FILTERS</span>
            <ChevronDoubleRightIcon className="w-4 h-10"/>
          </div>
        </Menu.Button>
        <Menu.Items className="flex flex-col w-full h-full bg-red-100 transition-all ease-in">
          {[1, 2, 3, 4, 5, 6].map((link) => (
            <Menu.Item
              as="a"
              className="w-10 h-5 ui-selected:bg-iso-blue-grey-100 ui-not-selected:bg-iso-white"
            ></Menu.Item>
          ))}
        </Menu.Items>
      </Menu>
    </div>
  );
}

export default FiltersColumn;
