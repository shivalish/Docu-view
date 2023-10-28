import { React, useContext, useEffect, useState } from "react";
import { Switch } from "@headlessui/react";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/solid";
import { fetchContext } from "./TableContext";
import classNames from "classnames";

//enables the filter
function ModifiedSwitch({ label, changer, cmd }) {
  const [open, setOpen] = useState(false);
  return (
    <Switch
      enabled={open}
      onChange={()=>{setOpen(!open); changer(cmd);}}
      className="flex flex-row items-center gap-6"
    >
      <span className="text-iso-white font-bold">{label}</span>
      <div className="flex h-5 w-10 bg-iso-blue-grey-200 rounded-full items-center">
        <span
          className={`${
            open ? "translate-x-6" : "translate-x-0"
          } inline-block h-4 w-4 transform rounded-full bg-white transition`}
        />
      </div>
    </Switch>
  );
}

//this is each row of the dropdown menu
//cmd is /api/v1/...
function FilterRow({ name, textboxes = false, setQuery }) {
  return (
    <div className="flex flex-col w-full py-2 items-center border-b-2 border-iso-white">
      <ModifiedSwitch label={name} changer={setQuery}/>
      {textboxes && (
        <div className="flex flex-row gap-4 w-full text-black">
          <input className="w-10 bg-iso-grey border-iso-blue-grey-100 border-2" />
          <input className="w-10 bg-iso-grey border-iso-blue-grey-100 border-2" />
        </div>
      )}
    </div>
  );
}

function Tester(){ //testing if useContext works
  

  const x = useContext(fetchContext)


  useEffect(()=>{
    console.log("query is changing!", x)
  }, [])
  return(
    <div>
      {x}
    </div>
  );
}

//dropdown menu
function FiltersColumn() {
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState("/api/v1/");


  return (
    <fetchContext.Provider value={query}>
      <Tester/>
      <div className="flex flex-col bg-iso-blue h-full w-full text-iso-white p-4">
        <div
          onClick={() => {
            setOpen(!open);
          }}
          className="flex flex-col"
        >
          <span className="flex flex-row w-full h-10 items-center text-lg">
            FILTERS
            <ChevronDoubleRightIcon
              className={classNames("w-6 h-6", open && "rotate-90")}
            />
          </span>
        </div>
        {open && (
          <div className="flex flex-col">
            {[
              {name: "Project Type", textboxes: false, setQuery: ()=>{setQuery("bruh")}},
              {name: "Attachment Type", textboxes: false, setQuery: ()=>{setQuery("bruh2")}},
              {name: "Auction Type", textboxes: true, setQuery: (date1, date2)=>{setQuery(`${date1, date2} bruh3`)}},
              {name: "Resource Type", textboxes: true, setQuery: ()=>{setQuery("bruh4")}}
            ].map((row) => (
              <FilterRow 
              {...row}
              />
            ))}
          </div>
        )}
      </div>
    </fetchContext.Provider>
  );
}

export default FiltersColumn;
