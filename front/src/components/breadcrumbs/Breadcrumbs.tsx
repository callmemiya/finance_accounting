import React from "react";
import useBreadcrumbs, {BreadcrumbData} from "use-react-router-breadcrumbs";
import CustomLink from "../custom-link/custom-link";
import {routesConfig} from "../../app/routes-config";

const Breadcrumbs = () => {

    const breadcrumbs: BreadcrumbData[] = useBreadcrumbs();

    const findRouteName = (route: any) => {
        const foundRoute = routesConfig.find(element => element.path === route);
        return foundRoute !== undefined ? foundRoute.name : "Страница не найдена";
    }

    return breadcrumbs !== undefined &&
    findRouteName(breadcrumbs[breadcrumbs.length - 1].location.pathname) !== "Страница не найдена" ?
        <div className={"hide-on-900px alm-header-section-title"}>
            <div className="h-100 d-flex align-items-center">
                {breadcrumbs.length === 1 ? (
                    // в случае, если пользователь находится на главной странице, необходимо отображать элемент "хлебные крошки" полностью
                    // в ином случае, необходимо отображать элемент "хлебные крошки" без корневой страницы "Платформа Казначейства"
                    breadcrumbs.map(({breadcrumb, match, key}, index) => (
                        <span key={(key)}>
                    <CustomLink enabled={index < breadcrumbs.length - 1}
                                to={match.pathname || ""}>{findRouteName(match.pathname)}</CustomLink>
                            &nbsp; {(index < breadcrumbs.length - 1) && ">"} &nbsp;
                </span>
                    ))) : (
                    breadcrumbs.map(({breadcrumb, match, key}, index) => (
                        <span key={key} hidden={index < 1}>
                    <CustomLink enabled={index < breadcrumbs.length - 1 && index > 0}
                                to={match.pathname || ""}>{findRouteName(match.pathname)}</CustomLink>
                            &nbsp; {(index < breadcrumbs.length - 1 && index > 0) && ">"} &nbsp;
                </span>
                    )))}
            </div>
        </div>
        : <></>;
};

export default Breadcrumbs;
