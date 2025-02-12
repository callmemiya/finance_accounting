import {type JSX, useMemo} from "react";
import {Navbar} from "react-bootstrap";
import {useLocation} from "react-router-dom";
import styles from "./Logo/Logo.module.css";

import "./header.css";
import {FinanceHeader} from "./finance-header";
import Breadcrumbs from "../components/breadcrumbs/Breadcrumbs";
import logo from "./Logo/finance-logo-1.png";
import {DiagramsHeader} from "./diagrams-header";

const headersMap: Record<string, () => JSX.Element> = {
    "finance": FinanceHeader,
    "diagrams": DiagramsHeader
} as const;

export function Header() {
    const {pathname} = useLocation();

    const path = useMemo(() => pathname.split('/')[1], [pathname]);

    const ElementToRender = useMemo(() => {
        const Header = headersMap[path];

        return Header ?? Breadcrumbs;
    }, [path]);

    return (
        <Navbar
            collapseOnSelect
            expand="md"
            className="alm-header alm-header-border justify-content-start px-4"
        >
            <div className="d-flex order-1">
                <a data-testid="alm-header-logo" className={styles.logo} href="/">
                    <img src={logo} alt="Financial Tracking Logo" style={{ width: '35px', height: '35px' }}/>
                </a>
                <Navbar.Toggle aria-controls="alm-header-navbar-collapse"/>
            </div>
            <Navbar.Collapse
                id="alm-header-navbar-collapse"
                className="order-3 order-md-2"
            >
                <ElementToRender/>
            </Navbar.Collapse>
        </Navbar>
    );
};
