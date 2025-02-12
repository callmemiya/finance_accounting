import React, {useCallback, useEffect, useState} from "react";
import { Nav, Navbar, NavDropdown, NavLink } from "react-bootstrap";

import "./header.css";
import {FinanceEventKey} from "../pages/finance/FinanceEventKey";
import {useHeaderRoutingHandler} from "../utils/routeUtils";
import {DiagramsEventKey} from "../pages/diagrams/DiagramsEventKey";

const rootPath = "/diagrams";

/**
 * Встраиваемый заголовок подсистемы Diagrams в основной заголовок {@code header.js}
 * */
export const DiagramsHeader = () => {
    const [key, handleOnSelect, resolveHref] = useHeaderRoutingHandler(
        DiagramsEventKey.DISCHARGE.value,
        rootPath
    );

    return (
        <Nav
            className={"align-items-md-center"}
            activeKey={key}
            onSelect={handleOnSelect}
        >
            <Navbar.Brand className="hide-on-900px alm-header-section-title">
                Диаграммы
            </Navbar.Brand>
            <Nav.Link
                className="alm-header-nav-link"
                href={resolveHref(DiagramsEventKey.DISCHARGE.to())}
                eventKey={DiagramsEventKey.DISCHARGE.value}
            >
                Расходы
            </Nav.Link>
            <Nav.Link
                className="alm-header-nav-link"
                href={resolveHref(DiagramsEventKey.GAIN.to())}
                eventKey={DiagramsEventKey.GAIN.value}
            >
                Доходы
            </Nav.Link>
        </Nav>
    );
};
