import React from 'react';

import Header from './atoms/Header';
import FiltersColumn from './components/FiltersColumn';
import FilesTable from './components/FilesTable';
import { FetchContextProvider } from './components/TableContext';

function MainPage() {
    return (
        <FetchContextProvider>
            <div className="h-full w-full flex flex-col">
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
        </FetchContextProvider>
    );
}

export default MainPage;