import React from "react";

import {BasicContainerTitle} from "./BasicContainerTitle";

import "./basic-containers.css"


type BasicContainerType = {
    title?: string;
    children?: any;
    className?: string;
    classNameTitle?: string;
    style?: React.CSSProperties;
    onClick?: (event: React.MouseEvent<HTMLDivElement>) => void;
    onAuxClick?: (event: React.MouseEvent<HTMLDivElement>) => void;
}

export const BasicContainer = (props: BasicContainerType) => {
    return <div className={"basic-container ".concat(props.className ?? "")}
                style={props.style}
                onClick={props.onClick}
                onAuxClick={props.onAuxClick}>
        {props.title ? <BasicContainerTitle title={props.title} className={props.classNameTitle}/> : null}
        {props.children}
    </div>
}