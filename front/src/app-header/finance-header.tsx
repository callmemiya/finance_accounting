import React, {useCallback, useEffect, useState} from "react";
import { Nav, Navbar, NavDropdown, NavLink } from "react-bootstrap";

import "./header.css";
import {FinanceEventKey} from "../pages/finance/FinanceEventKey";
import {useHeaderRoutingHandler} from "../utils/routeUtils";

const rootPath = "/finance";

/**
 * Встраиваемый заголовок подсистемы Finance в основной заголовок {@code header.js}
 * */
export const FinanceHeader = () => {
    const [key, handleOnSelect, resolveHref] = useHeaderRoutingHandler(
        FinanceEventKey.REPORTS.value,
        rootPath
    );

    const [isOpenReportsMenu, setIsOpenReportsMenu] = useState<boolean>(false);

    const clickReportsMenu = (newVal?: boolean) =>
        setIsOpenReportsMenu((prevState) => newVal ?? !prevState);

    return (
        <Nav
            className={"align-items-md-center"}
            activeKey={key}
            onSelect={handleOnSelect}
        >
            <Navbar.Brand className="hide-on-900px alm-header-section-title">
                Финансы
            </Navbar.Brand>
            <NavDropdown
                title="Отчёты"
                className={"alm-header-nav-link alm-header-dropdown"}
                show={isOpenReportsMenu}
                onToggle={(nextShow) => clickReportsMenu(nextShow)}
            >
                <NavDropdown.Item
                    className={
                        "alm-header-nav-link alm-header-dropdown-nav-link"
                    }
                    eventKey={FinanceEventKey.REPORTS.value}
                    href={resolveHref(FinanceEventKey.REPORTS.to())}
                    to="/finance"
                    as={NavLink}
                >
                    Операции
                </NavDropdown.Item>
                <NavDropdown.Item
                    className={
                        "alm-header-nav-link alm-header-dropdown-nav-link"
                    }
                    eventKey={FinanceEventKey.SALARY.value}
                    href={resolveHref(FinanceEventKey.SALARY.to())}
                    to="/finance"
                    as={NavLink}
                >
                    Зарплата
                </NavDropdown.Item>
            </NavDropdown>
            <Nav.Link
                className="alm-header-nav-link"
                href={resolveHref(FinanceEventKey.CATEGORIES.to())}
                eventKey={FinanceEventKey.CATEGORIES.value}
            >
                Категории
            </Nav.Link>
        </Nav>
    );
};
