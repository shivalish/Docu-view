import {React, createContext, useState } from 'react';

export const FetchContext = createContext();

export const FetchContextProvider = ({children}) => {
    const [val, setVal] = useState({});
    const superSetVal = (newVal) => setVal(newVal);

    return (<FetchContext.Provider value={{val, superSetVal}}>
        {children}
    </FetchContext.Provider>)
}
