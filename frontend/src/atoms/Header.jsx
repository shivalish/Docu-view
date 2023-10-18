import React from 'react';
import logo from '../imgs/logo.jpg';

export default function Header(){

    //TODO 6: add window size break thresholds (see tailwind docs) and make sure components are sized correctly
    return (<div className="w-full h-24 bg-iso-white px-5">
        <img src={logo} alt="iso new england logo"className="h-full w-42"/>
    </div>)
}