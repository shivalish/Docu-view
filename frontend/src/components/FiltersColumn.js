import { React, useContext, useEffect, useState } from "react";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/solid";
import { fetchContext } from "./TableContext";
import classNames from "classnames";
import { Disclosure } from "@headlessui/react";
import FilterTypes from "../atoms/FilterTypes";
import { Combobox } from "@headlessui/react";
import { Menu } from "@headlessui/react";
import Button from "../atoms/Button.jsx";
import SelectionTag from "../atoms/SelectionTag.jsx";
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
  placeholder = "",
  setQuery,
  api,
}) {
  //textbox states
  const [text, setText] = useState("");
  const [textlog, setTextLog] = useState(new Set());

  //combobox states
  const [selectedCombo, setCombo] = useState(null);
  const [comboText, setComboText] = useState("");
  const [combolog, setComboLog] = useState(new Set());

  //dropdown states
  const [currYear, setYear] = useState(new Set());

  //checkbox states
  const [selectedCheck, setCheck] = useState([]);

  const [vals, setVals] = useState(api);

  //TODO: code cleanup + visual alignment fixes

  //TODO: create a custom textbox component

  const filtered =
    comboText === ""
      ? combo
      : combo.filter((val) =>
          val.toLowerCase().includes(comboText.toLowerCase())
        );

  //adds a key/value pair to the global query variable
  const click = (selectedValue) => {
    setVals({ [Object.keys(vals)[0]]: selectedValue });
    setQuery(vals);
  };

  return (
    <div className="w-full">
      {checkbox.length > 0 && (
        <div>
          <Disclosure className="w-full">
            {({ open }) => (
              <div>
                <Disclosure.Button>
                  <span className="flex flex-row w-full h-10 items-center text-lg pl-2">
                    {name}
                    <ChevronDoubleRightIcon
                      className={classNames("w-6 h-6", open && "rotate-90")}
                    />
                  </span>
                </Disclosure.Button>
                <Disclosure.Panel>
                  <div className="flex flex-col gap-2 w-full">
                    {checkbox.map((docType, index) => (
                      <div>
                        <input
                          type="checkbox"
                          id={index}
                          checked={
                            selectedCheck.find((v) => v === docType) !==
                            undefined
                          }
                          className="form-checkbox mx-1"
                          onChange={(e) => {
                            e.target.checked
                              ? selectedCheck.push(docType)
                              : selectedCheck.splice(
                                  selectedCheck.indexOf(docType),
                                  1
                                );
                            click(selectedCheck);
                          }}
                        />
                        <label>{docType}</label>
                      </div>
                    ))}
                  </div>
                </Disclosure.Panel>
              </div>
            )}
          </Disclosure>
          {selectedCheck.length > 0 && (
            <div>
              {selectedCheck.map((e, index) => (
                <SelectionTag
                  key={index}
                  value={e}
                  onDelete={() => {
                    selectedCheck.splice(selectedCheck.indexOf(e), 1);
                    click(selectedCheck);

                    checkbox.forEach((box, i) => {
                      if (document.getElementById(i)) {
                        document.getElementById(i).checked =
                          selectedCheck.find((v) => v === box) !== undefined;
                      }
                    });
                  }}
                />
              ))}
            </div>
          )}
        </div>
      )}
      {dropdown.length > 0 && (
        <div className="bg-iso-blue w-full">
          <Menu>
            {({ open }) => (
              <div>
                <Menu.Button className="w-full h-10">
                  <span className="flex flex-row w-full h-10 items-center text-lg pl-2">
                    {name}
                    <ChevronDoubleRightIcon
                      className={classNames("w-6 h-6", open && "rotate-90")}
                    />
                  </span>
                </Menu.Button>
                <Menu.Items className="h-32 overflow-auto">
                  {dropdown.map((year) => (
                    <Menu.Item
                      onClick={() => {
                        currYear.add(year);
                        click(currYear);
                      }}
                    >
                      <div className="w-full justify-center items-center hover:text-blue-400">
                        <span className="cursor-pointer">{year}</span>
                      </div>
                    </Menu.Item> //TODO: overall style rework
                  ))}
                </Menu.Items>
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
                  onClick={() => combolog.add(val)}
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
                textlog.add(text)
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
      </Disclosure>
    </fetchContext.Provider>
  );
}

//TODO: remove tester component when implementing table
function Tester() {
  //testing if useContext works

  const x = useContext(fetchContext);

  useEffect(() => {
    console.log("query is changing!", x);
  }, [x]);
  return <div>{JSON.stringify(x)}</div>;
}

//dropdown menu
function FiltersColumn() {
  const [query, setQuery] = useState({});

  return (
    <fetchContext.Provider value={query}>
      <div className="flex flex-col bg-iso-blue h-[50rem] w-full text-iso-white p-4 gap-2 overflow-y-scroll">
          <div className="flex flex-col border-t">
            {FilterTypes.map((row) => (
              <div className={`py-2 border-b`}>
                <FilterRow
                  {...row}
                  setQuery={(values) => setQuery({ ...query, ...values })}
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
    </fetchContext.Provider>
  );
}

export default FiltersColumn;
