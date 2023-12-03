import {React, createContext, useState } from 'react';

export const FetchContext = createContext();

export const FetchContextProvider = ({children}) => {
    const [val, setVal] = useState({});
    const superSetVal = (newVal) => setVal(newVal);

    return (<FetchContext.Provider value={{val, superSetVal}}>
        {children}
    </FetchContext.Provider>)
}

export const SubmitContext = createContext();

export const SubmitContextProvider = ({children}) => {
    const [update, setUpdate] = useState(0);
    const superSetUpdate = (newVal) => setUpdate(newVal);

    return (<SubmitContext.Provider value={{update, superSetUpdate}}>
        {children}
    </SubmitContext.Provider>)
}
