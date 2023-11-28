import { React, useContext, useEffect, useState } from "react";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/solid";
import { FetchContext } from "./TableContext";
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
  const [textlog] = useState(new Set());

  //combobox states
  const [selectedCombo, setCombo] = useState(null);
  const [comboText, setComboText] = useState("");
  const [combolog] = useState(new Set());

  //dropdown states
  const [currYear] = useState(new Set());

  //checkbox states
  const [selectedCheck] = useState([]);

  //TODO: code cleanup + visual alignment fixes

  //TODO: create a separate custom dropdown component

  //TODO: createa custom textbox component
  //adds a key/value pair to the global query variable
  const click = (selectedValue) => {
    setQuery(selectedValue instanceof Set ? {[Object.keys(api)[0]]: [...selectedValue]} : { [Object.keys(api)[0]]: selectedValue });
  };

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
          </Menu>
          {currYear.size > 0 && (
            [...currYear].map((e, i)=> (
              <SelectionTag
              value={e}
              key={i}
              onDelete={() => {
                currYear.delete(e);
                click(currYear);
              }}
            />
            ))
          )}
        </div>
      )}
      {combo.length > 0 && (
        <div className="w-full p-2">
          <span className="text-lg"> {name} </span>
          <Combobox value={selectedCombo} onChange={setCombo}>
            <Combobox.Input
              onChange={(e) => setComboText(e.target.value)}
              className="textbox bg-iso-blue-grey-300 w-full max-w-full"
              onKeyUp={e => {
                if(e.key==='Enter' && selectedCombo !== null){
                  combolog.add(selectedCombo);
                  setComboText("");
                  setCombo(null);
                }}}
              placeholder={placeholder}
            />
            <Combobox.Options className="flex flex-col pt-1 gap-1">
              {filtered.map((val, index) => (
                <Combobox.Option
                  key={index}
                  value={val}
                  className="rounded-md ui-active:bg-iso-blue-grey-200
                  ui-active:text-white bg-iso-blue-grey-300
                  text-iso-white overflow-hidden text-sm p-1"
                  onClick={() => {combolog.add(val); click(combolog);}}
                >
                  {val}
                </Combobox.Option>
              ))}
            </Combobox.Options>
          </Combobox>
          {combolog.size > 0 && (
            [...combolog].map((e,i) => (
              <SelectionTag
                key={i}
                value={e}
                onDelete={() => {
                  combolog.delete(e);
                  click(combolog);
                }}
              />
            ))
          )}
        </div>
      )}

      {textbox && (
        <div className="w-full p-2">
          <label className="text-lg">{name}</label>
          <input
            className="textbox bg-iso-blue-grey-300"
            onChange={e => setText(e.target.value)}
            onKeyUp={e => {
              if(e.key==='Enter'){
                textlog.add(text);
                click(textlog);
                setText("");
              }}}
            value={text}
            placeholder={placeholder}
          />
          {textlog.size > 0 && (
            [...textlog].map((e, i) => (
              <SelectionTag
                key={i}
                value={e}
                onDelete={() => {
                  textlog.delete(e);
                  click(textlog);
                }}
              />
            ))
          )}
        </div>
      )}
    </Disclosure>
  );
}

//TODO: remove tester component when implementing table
// function Tester() {
//   //testing if useContext works

//   const x = useContext(fetchContext);
//   useEffect(() => {
//     console.log("query is changing!", x);
//   }, [x]);
//   return <div>{JSON.stringify(x)}</div>;
// }

//dropdown menu
function FiltersColumn() {
  const {val, superSetVal}= useContext(FetchContext);

  return (
      <div className="flex flex-col bg-iso-blue h-[50rem] w-full text-iso-white p-4 gap-2 overflow-y-scroll">
          <div className="flex flex-col border-t">
            {FilterTypes.map((row) => (
              <div className={`py-2 border-b`}>
                <FilterRow
                  {...row}
                  setQuery={(values) => (superSetVal({ ...val, ...values }))}
                />
              </div>
            ))}
          </div>

          <div className="flex justify-center">
            <Button
              onClick={() => console.log("do something")}
              height="h-full"
              width="w-1/2"
            >
              Submit
            </Button>
            </div>
      </div>
  );
}

export default FiltersColumn;
