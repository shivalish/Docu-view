import React from 'react';
import logo from '../imgs/logo.jpg'

function TopBar() {
    return (
        <div className="w-full h-full flex items-center justify-start bg-iso-white">
            <img
                src={logo}
                alt="Description of Image"
                className="ml-5"
            />
        </div>
    );
}

export default TopBar;
