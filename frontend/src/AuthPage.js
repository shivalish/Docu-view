import React from "react";
import Header from "./atoms/Header.jsx";
import Button from "./atoms/Button.jsx";
import { Tab } from "@headlessui/react";
import { useNavigate } from "react-router-dom";

function HomePageButton({ text, OnClick }){
  return (
      <div className="flex justify-end">
        <Button width="w-32" height="h-10" OnClick={OnClick}>
          { text }
        </Button>
      </div>
  )
}

// Moved it for clarity (used 4 times in the single part)
function HomePageInput( { label, hidden=false, forgot=false, forgotText } ){
  return (
    <div className="flex flex-col w-full">
      <label> {label} </label>
      <input type={hidden && "password"} className="h-10 w-full px-5 bg-iso-grey rounded-md"/>
      {forgot && (<span className="h-10 text-right">{forgotText}</span>)}
    </div>
  )
}

function HomePageTabs(){
  const navigate = useNavigate();
  const movePage = () => navigate('/main');
  return (
    <div className="flex h-full w-1/3 justify-center items-center">
      <div>
        <Tab.Group>
          <Tab.List>
            {["Login", "Sign Up"].map((txt)=>(
              <Tab className={`rounded-t-md ui-selected:bg-iso-white ui-not-selected:bg-iso-grey h-10 w-32`}>
                {txt}
              </Tab>
            ))}
          </Tab.List> 
          <Tab.Panels>
            <Tab.Panel className="w-96 h-1/3 bg-iso-white p-10 rounded-b-md rounded-tr-md">
              <HomePageInput label="Username" forgot={true} forgotText={"Forgot Username"}/>
              <HomePageInput label="Password" hidden={true} forgot={true} forgotText={"Forgot Password"}/>
              <HomePageButton text="Login" OnClick={movePage}/>
            </Tab.Panel>
            <Tab.Panel className="flex flex-col gap-4 w-96 h-1/3 bg-iso-white p-10 rounded-b-md rounded-tr-md">
              <HomePageInput label="Username" />
              <HomePageInput label="Password" />
              <HomePageButton text="Sign Up" OnClick={movePage}/>
            </Tab.Panel>
          </Tab.Panels>
        </Tab.Group>
      </ div>
    </div>
  )
}

function HomePage() {
  //TODO 1: reduce boilerplate html; too many divs so u would need to move some divs into components folder
  //TODO 2: reduce boilerplate tailwind css; maybe add a tailwind class to index.css and use here
  //TODO 3: implement text rememberance when switching tabs
  //TODO 4: tweak font sizes and standardize font

  return (
    <div className="flex flex-col w-screen h-screen bg-iso-blue">
      <Header />
      <div className="flex flex-grow items-center justify-center">
        <div className="w-1/3"></div>
        <HomePageTabs />
        <div className="w-1/3"></div>
      </div>
      <div className="flex-grow"></div>
    </div>
  );
  
}

export default HomePage;
