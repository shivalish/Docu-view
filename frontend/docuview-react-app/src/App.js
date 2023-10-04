import logo from './logo.jpg';

import './App.css';
import React, { useState } from 'react'

function App() {
  const [log, setLog] = useState([])

  const pingServer = async () => {
    let response;
    try {
      response = await fetch('http://localhost:8080/docuPing');
      let data = await response.json();
      if (data.serverPong && data.databasePong) {
        setLog((oldLog) => {
          let newLog = [...oldLog]
          newLog.push(`SUCCESS: server says '${data.serverPong}' and database says: '${data.databasePong}'!`);
          return newLog;
        })
      } 
    } catch (error) {
      console.log(error)
      setLog((oldLog) => {
        let newLog = [...oldLog]
        newLog.push("SERVER ERROR: server did not respond.")
        return newLog;
      })
    }
  }

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Docuview react app boilerplate code.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <br />
        <button
          onClick={pingServer}
        >
          docu-Ping server
          </button>
        <p>{log.map((msg, index) => <p key={index}>{msg}</p>)}</p>
      </header>
    </div>
  );
}

export default App;
