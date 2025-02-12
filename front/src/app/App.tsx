import React from "react";
import NiceModal from "@ebay/nice-modal-react";
import AppRouter from "./AppRouter";
import { UserContext } from "../contexts/UserContext";
import {BrowserRouter} from "react-router-dom";



const App = () => {

    return <UserContext.Provider value={null}>
        <NiceModal.Provider>
            <BrowserRouter>
                <AppRouter/>
            </BrowserRouter>
        </NiceModal.Provider>
    </UserContext.Provider>
}

export default App;
