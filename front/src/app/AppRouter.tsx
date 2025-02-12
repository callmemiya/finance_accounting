import React from 'react';
import '../App.css';
import {Routes, Route, useLocation} from "react-router-dom";
import NoPage from "../pages/nopage";
import {WorkspaceLayout} from "../workspace/WorkspaceLayout";
import {BasicContainer} from "../workspace/BasicContainer";
import Home from "../home/home";
import Layout from "../layout/layout";
import {Finance} from "../pages/finance/finance";
import {Diagrams} from "../pages/diagrams/diagrams";

function AppRouter() {

    let location = useLocation();

    return (
        <>
            <link
                rel="stylesheet"
                type="text/css"
                href={
                    // location.pathname === "/sul"
                    //     ? "/css/dx.material.blue.light.compact.css"
                        "/css/dx.material.orange.light.compact.css"
                }
            />
            <Routes>
                <Route path="/" element={<Layout location={location}/>}>
                    <Route index element={
                        <BasicContainer>
                            <WorkspaceLayout>
                                <Home/>
                            </WorkspaceLayout>
                        </BasicContainer>
                    }/>
                    <Route path="finance" element={
                        <BasicContainer>
                            <Finance/>
                        </BasicContainer>
                    }/>
                    <Route path="diagrams" element={
                        <BasicContainer>
                            <Diagrams/>
                        </BasicContainer>
                    }/>
                </Route>
                <Route
                    path={"*"}
                    element={<Layout location={location} isNotFound/>}
                >
                    <Route path={"*"} element={<NoPage/>}/>
                </Route>
            </Routes>
        </>
    );
}

export default AppRouter;
