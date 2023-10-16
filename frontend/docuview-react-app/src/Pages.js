import React, { useState } from 'react'
import MainPage from './MainPage';
import HomePage from './HomePage';

function Pages() {

    const [page, setPage] = useState("Home")

    function getPage() {
        switch (page) {
            case "Main":
                return <MainPage setPage={setPage} />
            case "Home":
                return <HomePage setPage={setPage} />
            default:
                return <MainPage setPage={setPage} />
        }
    }


    return (
        <div class='w-screen h-screen'>
            {getPage()}
        </div>
    )
}

export default Pages;