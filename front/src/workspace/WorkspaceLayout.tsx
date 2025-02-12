import {Container} from "react-bootstrap";
import "./workspace.css"
import React from "react";
import {BasicContainerTitle} from "./BasicContainerTitle";

type WorkspaceLayoutProps = {
    children?: any;
    titleName?: string;
    debugProps?: WorkspaceLayoutDebugProps;
    className?: string;
    id?: string;
}

type WorkspaceLayoutDebugProps = {
    showBackground?: boolean;
}

export const WorkspaceLayout = (props: WorkspaceLayoutProps) =>
    <Container id={"alm-workspace-layout"+(props.id ? props.id : "")} fluid style={{
        backgroundColor: props.debugProps?.showBackground ? "blueviolet" : ""
    }} className={"d-flex flex-column " + (props.className ? props.className : "")}>
        {props.titleName ? <BasicContainerTitle title={props.titleName}/> : undefined}
        {props.children}
    </Container>