import React, { useState } from "react";
import Header from "./atoms/Header.jsx";
import Button from "./atoms/Button.jsx";
import { Tab } from "@headlessui/react";
import { useNavigate } from "react-router-dom";

function HomePageButton({ text, OnClick }) {
  return (
    <div className="flex justify-end py-1">
      <Button width="w-32" height="h-10" OnClick={OnClick}>
        {text}
      </Button>
    </div>
  );
}

// Moved it for clarity (used 4 times in the single part)
function HomePageInput({ label, hidden = false, forgot = false, userInput = "", setUserInput = null, forgotText }) {
  if (setUserInput === null) {
    return (
      <div className="flex flex-col w-full">
      <label> {label} </label>
      <input
        type={hidden ? "password" : "text"}
        className="h-10 w-full px-5 bg-iso-grey rounded-md"
      />
      {forgot && <span className="h-5 text-right">{forgotText}</span>}
    </div>
    )
  }
  return (
    <div className="flex flex-col w-full">
      <label> {label} </label>
      <input
        type={hidden ? "password" : "text"}
        className="h-10 w-full px-5 bg-iso-grey rounded-md"
        onChange={(e) => {setUserInput(e.target.value)}}
        value={userInput}
      />
      {forgot && <span className="h-5 text-right">{forgotText}</span>}
    </div>
  );
}

function HomePageTabs() {
  const navigate = useNavigate();
  const movePage = () => navigate("/main");
  const [logInID, setLogInID] = useState("");
  const [logInPasswd, setLogInPasswd] = useState("");
  const [signUpID, setSignUpID] = useState("");
  const [signUpPasswd, setSignUpPasswd] = useState("");
  const [signUpPasswdReEnter, setSignUpPasswdReEnter] = useState("");
  return (
    <div className="flex h-full w-1/3 justify-center items-center">
      <div>
        <Tab.Group>
          <Tab.List>
            {["Login", "Sign Up"].map((txt) => (
              <Tab className={"tab"}>{txt}</Tab>
            ))}
          </Tab.List>
          <Tab.Panels>
            <Tab.Panel className="tab-body">
              <HomePageInput
                label="Username"
                forgot={true}
                forgotText={"Forgot Username"}
                userInput={logInID}
                setUserInput={setLogInID}
              />
              <HomePageInput
                label="Password"
                hidden={true}
                forgot={true}
                forgotText={"Forgot Password"}
                userInput={logInPasswd}
                setUserInput={setLogInPasswd}
              />
              <HomePageButton text="Login" OnClick={movePage} />
            </Tab.Panel>
            <Tab.Panel className="flex flex-col tab-body gap-5">
              <HomePageInput label="Username" 
              userInput={signUpID}
              setUserInput={setSignUpID}/>

              <HomePageInput label="Password" hidden={true} 
              userInput={signUpPasswd}
              setUserInput={setSignUpPasswd}/>

              <HomePageInput label="Re-enter Password" hidden={true}
              userInput={signUpPasswdReEnter}
              setUserInput={setSignUpPasswdReEnter}/>

              <HomePageButton text="Sign Up" OnClick={movePage} />
            </Tab.Panel>
          </Tab.Panels>
        </Tab.Group>
      </div>
    </div>
  );
}

function HomePage() {
  //TODO 3: implement text rememberance when switching tabs

  return (
    <div className="ffc w-screen h-screen bg-iso-blue">
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
