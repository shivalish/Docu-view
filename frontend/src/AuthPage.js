import React from "react";
import Header from "./atoms/Header.jsx";
import Button from "./atoms/Button.jsx";
import { Tab } from "@headlessui/react";
import { useNavigate } from "react-router-dom";

// COMPONENTS TO MOVE //

// Buttons can do things, but where are you going to get the data from ?
function HomePageButton({ text, OnClick }){
  return (
      <div className="flex justify-end">
        <Button width="w-32" height="h-10" OnClick={OnClick}>
          {" "}
          { text }{" "}
        </Button>
      </div>
  )
}

// Moved it for clarity (used 4 times in the single part)
function HomePageInput( { label } ){
  return (
    <><label> {label} </label><input className="h-10 w-full px-5 bg-iso-grey rounded-md" /></>
  )
}

//////////////////////


// Forgot Username and Forgot Password do nothing ?

// Do what you will with this, idk what exactly you want to do

function HomePageTabs(){
  const navigate = useNavigate();

  return (
    <div className="flex h-full w-1/3 justify-center items-center">
      <div>
        <Tab.Group>
          <Tab.List>
            <Tab
              className={`rounded-t-md ui-selected:bg-iso-white ui-not-selected:bg-iso-grey h-10 w-32`}
            >
              Login
            </Tab>
            <Tab className= {"rounded-t-md ui-selected:bg-iso-white ui-not-selected:bg-iso-grey h-10 w-32"}>
              Sign Up
            </Tab>
          </Tab.List> 
          <Tab.Panels>
            <Tab.Panel className="w-96 h-1/3 bg-iso-white p-10 rounded-b-md rounded-tr-md">
              <HomePageInput label="Username" />
              <div className="flex w-full h-10 justify-end">
                {" "}
                <span> Forgot Username </span>{" "}
              </div>
              <HomePageInput label="Password" />
              <div className="flex w-full h-10 justify-end">
                {" "}
                <span> Forgot Password </span>{" "}
              </div>
              <HomePageButton text="Login" OnClick={() => navigate('/main')}/>
            </Tab.Panel>
            <Tab.Panel className="w-96 h-1/3 bg-iso-white p-10 rounded-b-md rounded-tr-md">
              <HomePageInput label="Username" />
              <div className="flex w-full h-10 justify-end">
                {" "}
                {" "}
              </div>
              <HomePageInput label="Password" />
              <div className="flex w-full h-10 justify-end">
                {" "}
                {" "}
              </div>
              <HomePageButton text="Sign Up" OnClick={() => navigate('/main')}/>
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