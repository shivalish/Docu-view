import React from 'react';
import logo from '../imgs/logo.jpg';

export default function Header(){
    return (<div className="w-full h-auto bg-iso-white">
        <img src={logo} alt="iso new england logo"className="h-full p-4 w-42"/>
        <div className="w-full h-12 bg-iso-blue" />
    </div>)
}