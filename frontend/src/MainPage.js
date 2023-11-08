import React from 'react';

import Header from './atoms/Header';
import FiltersColumn from './components/FiltersColumn';
import FilesTable from './components/FilesTable';

function MainPage() {
    return (
        <div className="h-full w-full flex flex-col overflow-hidden">
            <div className="flex-shrink-0 ">
                <Header />
            </div>

            <div className="flex-grow flex">
                <div className="flex-none w-1/6">
                    <FiltersColumn />
                </div>

                <div className="flex-grow w-5/6">
                    <FilesTable />
                </div>

            </div>
        </div>
    );
}

export default MainPage;