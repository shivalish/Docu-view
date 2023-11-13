import { React, useState } from 'react';
import { Switch } from '@headlessui/react';

export default function ToggleSwitch({ changer }) {
    //TODO: align string name with button such that all buttons are aligned
    const [open, setOpen] = useState(false);
    return (
      <Switch
        enabled={open}
        onChange={() => {
          setOpen(!open);
          changer();
        }}
        className="flex flex-row items-center gap-6"
      >
        <span className="text-iso-white font-bold">Enabled</span>
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