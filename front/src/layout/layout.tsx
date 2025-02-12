import React from "react";
import {Outlet} from "react-router-dom";
import {Location} from "history";
import {Header} from "../app-header/header";


type LayoutProps = {
    location: Location;
    isNotFound?: boolean;
};
/**
 * Общий компонент-подложка, на котором располагаются экранные формы
 * @see header.js
 * @see sul.tsx
 * @param layoutProps свойства компонента
 * @returns {JSX.Element} базовый контейнер, в котором отрисовываются все компоненты и хедер
 * @constructor
 */
const Layout = (layoutProps: LayoutProps) => {
    return (
        <main id="outer-container">
            <Header/>
            <Outlet/>
        </main>
    );
};


export default Layout;
