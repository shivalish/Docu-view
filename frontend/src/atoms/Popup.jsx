import React from "react";
import { Dialog } from "@headlessui/react";

//TODO: animate backdrop instead of sudden darken
export default function Popup({ children, onOpen, onClose }) {
  return (
    <Dialog
      open={onOpen}
      onClose={onClose}
      className="flex flex-wrap h-screen w-screen items-center justify-center absolute bottom-0 left-0"
    >
        <div className="opacity-70 bg-black h-screen w-screen absolute bottom-0 left-0"></div>
        <Dialog.Panel className="z-10">
            {children}
        </Dialog.Panel>
    </Dialog>
  );
}
