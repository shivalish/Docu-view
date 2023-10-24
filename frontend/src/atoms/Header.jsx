import React from 'react';
import logo from '../imgs/logo.jpg';
import { useNavigate } from "react-router-dom";

// I modified this a litttle so that clicking the icon helps return to the home page
export default function Header(){
    const navigate = useNavigate();
    //TODO 6: add window size break thresholds (see tailwind docs) and make sure components are sized correctly
    return (<div className="w-full h-24 bg-iso-white px-5">
        <img src={logo} alt="iso new england logo"className="h-full w-42"
        onClick={() => navigate('/')}/>
    </div>)
}