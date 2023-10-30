import { React, useState } from "react";
import { Switch } from "@headlessui/react";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/solid";
import classNames from "classnames";

function ModifiedSwitch({ label }) {
  const [open, setOpen] = useState(false);
  return (
    <Switch
      enabled={open}
      onChange={setOpen}
      className="ffr center gap-6"
    >
      <span className="text-iso-white font-bold">{label}</span>
      <div className="flex h-5 w-10 isob-grey-200 rounded-full center">
        <span
          className={`${
            open ? "translate-x-6" : "translate-x-0"
          } inline-block h-4 w-4 transform rounded-full bg-white transition`}
        />
      </div>
    </Switch>
  );
}

function FiltersColumn() {
  const [open, setOpen] = useState(false);

  return (
    <div className="ffc isob h-full w-full text-iso-white p-4">
      <div
        onClick={() => {
          setOpen(!open);
        }}
        className="ffc"
      >
        <span className="ffr w-full h-10 center text-lg">
          FILTERS
          <ChevronDoubleRightIcon
            className={classNames("w-6 h-6", open && "rotate-90")}
          />
        </span>
      </div>
      {open && (
        <div className="ffc">
          {[
            "Project Type",
            "Attachment Type",
            "Auction Type",
            "Resource Type",
          ].map((name) => (
            <div className="flex w-full h-16 center border-b-2 border-iso-white">
              <ModifiedSwitch label={name} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default FiltersColumn;
