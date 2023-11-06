import { React, useContext, useEffect, useState } from "react";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/solid";
import { fetchContext } from "./TableContext";
import classNames from "classnames";
import { Disclosure } from "@headlessui/react";
import FilterTypes from "../atoms/FilterTypes";
import { Combobox } from "@headlessui/react";
import { Menu } from "@headlessui/react";
import Button from "../atoms/Button.jsx";
// import Calendar from 'react-calendar';

//this is each row of the dropdown menu
//textboxes = 1 or 2 dictactes how many textboxes there will be
//setQuery is the function called (might have args)
function FilterRow({
  name,
  textbox = false,
  checkbox = [],
  combo = [],
  dropdown = [],
  setQuery,
}) {
  //textbox states
  const [text, setText] = useState("");

  //combobox states
  const [selectedCombo, setCombo] = useState(null);
  const [comboText, setComboText] = useState("");

  //dropdown states
  const [currYear, setYear] = useState(1900);

  //TODO: code cleanup + visual alignment fixes

  //TODO: create a custom textbox component

  const filtered =
    comboText === ""
      ? combo
      : combo.filter((val) =>
          val.toLowerCase().includes(comboText.toLowerCase())
        );

  useEffect(() => {
    setQuery(text);
  }, [text]); //auto change query on textbox change

  return (
    <div>
      {dropdown.length > 0 && (
        <div>
          <Menu>
            {({ open }) => (
              <div>
                <Menu.Button className="w-full h-10">
                  <span className="flex flex-row w-full h-10 items-center text-lg">
                    {name}
                    <ChevronDoubleRightIcon
                      className={classNames("w-6 h-6", open && "rotate-90")}
                    />
                  </span>
                </Menu.Button>
                <Menu.Items className="h-32 overflow-auto">
                  {dropdown.map((year) => (
                    <Menu.Item onClick={()=>setQuery()}>
                      <div className="w-full justify-center items-center hover:text-blue-400">
                        {year}
                      </div>
                    </Menu.Item> //TODO: overall style rework
                  ))}
                </Menu.Items>
              </div>
            )}
          </Menu>
        </div>
      )}
      {combo.length > 0 && (
        <div>
          <span> {name} </span>

          <Combobox value={selectedCombo} onChange={setCombo}>
            <Combobox.Input onChange={(e) => setComboText(e.target.value)} />
            <Combobox.Options>
              {filtered.map((val, index) => (
                <Combobox.Option
                  key={index}
                  value={val}
                  className="ui-active:bg-blue-500 ui-active:text-white ui-not-active:bg-white ui-not-active:text-black"
                  onClick={()=>setQuery()}
                >
                  {val}
                </Combobox.Option>
              ))}
            </Combobox.Options>
          </Combobox>
        </div>
      )}

      {textbox && (
        <div>
          <label> {name} </label>
          <input
            className="w-full bg-iso-blue-grey-300 border-iso-blue-grey-100 border-2"
            onChange={(e) => {
              setText(e.target.value);
            }}
          />
        </div>
      )}

      {checkbox.length > 0 && (
        <Disclosure>
          {({ open }) => (
            <div>
              <Disclosure.Button>
                <span className="flex flex-row w-full h-10 items-center text-lg">
                  {name}
                  <ChevronDoubleRightIcon
                    className={classNames("w-6 h-6", open && "rotate-90")}
                  />
                </span>
              </Disclosure.Button>
              <Disclosure.Panel>
                <div className="flex flex-col gap-2 w-full">
                  {checkbox.map((docType) => (
                    <div>
                      <label>{docType}</label>
                      <input type="checkbox" className="form-checkbox" onClick={()=>setQuery()}/>
                    </div>
                  ))}
                </div>
              </Disclosure.Panel>
            </div>
          )}
        </Disclosure>
      )}
    </div>
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
        <div className="flex flex-col bg-iso-blue h-full w-full text-iso-white p-4 gap-2">
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
                  setQuery={() => {
                    setQuery(query + row.api);
                  }}
                />
              ))}
            </div>
          </Disclosure.Panel>

          <Button
            onClick={() => console.log("do something")}
            width={"w-full"}
            height={"h-10"}
          >
            GO
          </Button>
        </div>
      </Disclosure>
    </fetchContext.Provider>
  );
}

export default FiltersColumn;
