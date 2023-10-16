import React from "react";
import Header from "./atoms/Header.jsx";
import Button from "./atoms/Button.jsx";
import { Tab } from "@headlessui/react";

function HomePageLoginSignupTab(text){
  return ( 
    <Tab className={`rounded-t-md ui-selected:bg-iso-white ui-not-selected:bg-iso-grey h-10 w-32`} >
      { text }
    </Tab> 
  );
}

function HomePageBotton({ text, OnClick }){
  return (
      <Button width="w-32" height="h-10" OnClick={() => OnClick}>
        {" "}
        { text }{" "}
      </Button>
  )
}

// MAKE SIGNUP TOO

function HomePageTabs(){
  return (
    <Tab.Panels>
      <Tab.Panel className="w-96 h-1/3 bg-iso-white p-10 rounded-b-md rounded-tr-md">
        <label> Username </label>
        <input className="h-10 w-full px-5 bg-iso-grey rounded-md" />
        <div className="flex w-full h-10 justify-end">
          {" "}
          <span> Forgot Username </span>{" "}
        </div><label> Password </label><input className="h-10 w-full px-5 bg-iso-grey rounded-md" /><div className="flex w-full h-10 justify-end">
          {" "}
          <span> Forgot Password </span>{" "}
        </div>
        <div className="flex justify-end">
          <HomePageBotton text="Login" />
        </div>
      </Tab.Panel>
      <Tab.Panel className="w-96 h-1/3 bg-iso-white p-10 rounded-b-md rounded-tr-md">
        <label> Username </label>
        <input className="h-10 w-full px-5 bg-iso-grey rounded-md" />
        <div className="flex w-full h-10 justify-end">
          {" "}
          {" "}
        </div><label> Password </label><input className="h-10 w-full px-5 bg-iso-grey rounded-md" /><div className="flex w-full h-10 justify-end">
          {" "}
          {" "}
        </div>
        <div className="flex justify-end">
          <HomePageBotton text="Sign Up" />
        </div>
      </Tab.Panel>
    </Tab.Panels> 
  )
}

function HomePage() {
  //TODO 1: reduce boilerplate html; too many divs so u would need to move some divs into components folder
  //TODO 2: reduce boilerplate tailwind css; maybe add a tailwind class to index.css and use here
  //TODO 3: implement text rememberance when switching tabs
  //TODO 4: tweak font sizes and standardize font

  return (
    <div className="flex flex-wrap w-screen h-screen bg-iso-blue">
      <Header />
      <div className="h-full w-1/3" />
      <div className="flex h-full w-1/3 justify-center items-center">
        <div>
          <Tab.Group>
          <Tab.List>
            { [ "Login", "Signup" ].map(x => HomePageLoginSignupTab(x)) }
          </Tab.List> 
          < HomePageTabs />
          </Tab.Group>
        </div>
      </div>

      <div className="h-full w-1/3" />
    </div>
  );
}

export default HomePage;
