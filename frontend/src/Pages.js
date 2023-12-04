import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MainPage from './MainPage';
import AuthPage from './AuthPage';

function Pages() {
    return (
        <Router>
            <div className='w-screen h-screen overflow-hidden'>
                <Routes>
                    <Route path="/" element={<AuthPage />} />
                    <Route path="/main" element={<MainPage />} />
                </Routes>
            </div>
        </Router>
    );
}

export default Pages;
