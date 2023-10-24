import React, {useState} from "react";
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
// You can now put in a state variable to userInput and a function that modifies the state variable to setUserInput. 
// If a user types anything in this box, it will be reflected on the userInput variable
// If isPassword is true, it masks the password box
// If you don't pass any arguments, it does the same thing it did in the previous version and nothing bad happens
function HomePageInput( { label, userInput="", setUserInput = null, isPassword = false} ){
  let input_type;
  if (isPassword) {  
    input_type = "password";
  }
  else {
    input_type = "text";
  }
  if (setUserInput === null) {
    return (
      <><label> {label} </label>
      <input className="h-10 w-full px-5 bg-iso-grey rounded-md" 
      type = {input_type}/></>
    )
  }
  const handleInputChange = (event) => {
    setUserInput(event.target.value);
  };

  return (
    <><label> {label} </label>
    <input className="h-10 w-full px-5 bg-iso-grey rounded-md"
    type = {input_type}
    value = {userInput}
    onChange={handleInputChange} />
    </>
  )
}
//////////////////////


// Forgot Username and Forgot Password do nothing ?

// Do what you will with this, idk what exactly you want to do

// I modified the sign up page a little to the way I like
function HomePageTabs(){
  const navigate = useNavigate();
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");

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

              <HomePageInput label="Username" 
              userInput={username} 
              setUserInput={setUserName}/>

              <div className="flex w-full h-10 justify-end">
                {" "}
                <span> Forgot Username </span>{" "}
              </div>

              <HomePageInput label="Password" 
              userInput={password} 
              setUserInput = {setPassword}
              isPassword = {true}
              />

              <div className="flex w-full h-10 justify-end">
                {" "}
                <span> Forgot Password </span>{" "}
              </div>

              {/*I guess the authentication is this step*/}
              <HomePageButton text="Login" OnClick={() => navigate('/main')}/> 

            </Tab.Panel>

            <Tab.Panel className="w-96 h-1/3 bg-iso-white p-10 rounded-b-md rounded-tr-md">
              <HomePageInput label="Username" />

              <div className="flex w-full h-5 justify-end">
                {" "}
              </div>

              <HomePageInput label="Password" isPassword={true}/>
              <div className="flex w-full h-5 justify-end">
                {" "}
              </div>

              <HomePageInput label="Re-enter Password" isPassword={true}/>
              <div className="flex w-full h-5 justify-end">
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
