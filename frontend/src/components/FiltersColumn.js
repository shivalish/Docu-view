import { React, useContext, useEffect, useState } from "react";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/solid";
import { fetchContext } from "./TableContext";
import classNames from "classnames";
import ToggleSwitch from "../atoms/ToggleSwitch";
import { Disclosure } from "@headlessui/react";
import { Popover } from "@headlessui/react";
import FilterTypes from "../atoms/FilterTypes";
import {CalendarIcon} from "@heroicons/react/24/solid"; //this may be used later to clean up the calendar
import Calendar from 'react-calendar';

//this is each row of the dropdown menu
//textboxes = 1 or 2 dictactes how many textboxes there will be
//setQuery is the function called (might have args)
function FilterRow({ name, textboxes = 0, checkbox = [], calendar, setQuery }) {
  //TODO: simplify this into 1 state that uses an object instead of string => {text1: "example", text2: "something"};
  const [text, setText] = useState("");
  const [text2, setText2] = useState("");

  //TODO: implement the calendar functionality

  //TODO: code cleanup + visual alignment fixes

  //TODO: create a separate custom dropdown component

  //TODO: createa custom textbox component

  useEffect(() => {
    setQuery(text, text2);
  }, [text, text2]); //auto change query on textbox change

  return (
    <Disclosure>
      {({ open }) => (
        <>
          <Disclosure.Button className="flex flex-col w-full py-2 items-center gap-4">
            <span className="flex flex-row w-full h-10 items-center text-lg">
              {name}
              <ChevronDoubleRightIcon
                className={classNames("w-6 h-6", open && "rotate-90")}
              />
            </span>
          </Disclosure.Button>

          <Disclosure.Panel>
            <ToggleSwitch changer={setQuery} />
            {textboxes === 2 && (
              <div className="flex flex-row gap-4 w-full">
                <input
                  className="w-16 bg-iso-blue-grey-300 border-iso-blue-grey-100 border-2"
                  onChange={(e) => {
                    setText(e.target.value);
                  }}
                />
                <input
                  className="w-16 bg-iso-blue-grey-300 border-iso-blue-grey-100 border-2"
                  onChange={(e) => {
                    setText2(e.target.value);
                  }}
                />
              </div>
            )}

            {textboxes === 1 && (
              <div>
                <input
                  className="w-10 bg-iso-blue-grey-300 border-iso-blue-grey-100 border-2"
                  onChange={(e) => {
                    setText(e.target.value);
                  }}
                />
              </div>
            )}

            {checkbox.length > 0 && (
              <div className="flex flex-col gap-2 w-full">
                {checkbox.map((docType) => (
                  <div>
                    <label>{docType}</label>
                    <input type="checkbox" className="form-checkbox" />
                  </div>
                ))}
              </div>
            )}

            {calendar && (
              <div>
                <Calendar />
              </div>
            )}
          </Disclosure.Panel>
        </>
      )}
    </Disclosure>
  );
}

//TODO: remove tester component when implementing table
function Tester() {
  //testing if useContext works

  const x = useContext(fetchContext);

  useEffect(() => {
    console.log("query is changing!", x);
  }, [x]);
  return <div>{x}</div>;
}

//dropdown menu
function FiltersColumn() {
  const [query, setQuery] = useState("/api/v1/");

  return (
    <fetchContext.Provider value={query}>
      <Disclosure>
        <div className="flex flex-col bg-iso-blue h-full w-full text-iso-white p-4">
        <Disclosure.Button>
          <span className="flex flex-row w-full h-10 items-center text-lg">
            FILTERS
            <ChevronDoubleRightIcon
              className={classNames("w-6 h-6 ui-open:rotate-90")}
            />
          </span>
        </Disclosure.Button>

        <Disclosure.Panel>
          <div className="flex flex-col">
            {FilterTypes.map((row) => (
              <FilterRow
                {...row}
                setQuery={(date1 = null, date2 = null) => {
                  setQuery(`${date1} ${date2} ${row.api}`);
                }}
              />
            ))}
          </div>
        </Disclosure.Panel>
        </div>
      </Disclosure>
    </fetchContext.Provider>
  );
}

export default FiltersColumn;
