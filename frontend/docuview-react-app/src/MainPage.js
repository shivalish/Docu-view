import React from 'react';

import TopBar from './MainPageComponents/TopBar';
import FiltersColumn from './MainPageComponents/FiltersColumn';
import FilesList from './MainPageComponents/FilesList';

function MainPage() {
    return (
        <div className="h-full w-full flex flex-col">
            <div className="flex-shrink-0 h-1/6">
                <TopBar />
            </div>

            <div className="flex-grow h-1/6 flex">
                <div className="flex-none w-1/6">
                    <FiltersColumn />
                </div>

                <div className="flex-grow w-5/6">
                    <FilesList />
                </div>

            </div>
        </div>
    );
}

export default MainPage;
