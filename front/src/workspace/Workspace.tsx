import React from "react";
import {Container} from "react-bootstrap";
import {Button} from "devextreme-react";

type WorkspaceProps = {
    children?: any;
    className?: string;
    flexRow?: boolean;
    flexDontGrow?: boolean;
    filterBox?: boolean;
}

export const Workspace = (props: WorkspaceProps) => {
    const flexDirection = props.flexRow ? "flex-row" : "flex-column";
    const flexGrow = props.flexDontGrow ? "" : "flex-grow-1";

    // Separate the children elements into leftElements and rightElements
    const leftElements: React.ReactNode[] = [];
    const rightElements: React.ReactNode[] = [];

    if (props.filterBox) {
        React.Children.map(props.children, (child) => {
            // Check the type of the child element
            if (React.isValidElement(child)) {
                const elementType = child.type;

                // Determine if the element should be on the left or right side
                console.debug("elementType:" + elementType);
                if (elementType !== Button) {
                    leftElements.push(child);
                } else {
                    rightElements.push(child);
                }
            }
        });
    }

    return <Container fluid
                      className={`alm-workspace d-flex ${flexDirection} ${flexGrow} ${props.filterBox ? "justify-content-between" : ""} `
                          .concat(props.className ?? "")}>

        {props.filterBox ? (<>
            <div className="d-flex">
                {/* Render elements on the left */}
                {leftElements.map((element, index) => (
                    <div key={index} className="mr-3 me-3">{element}</div>
                ))}
            </div>

            <div className="d-flex">
                {/* Render elements on the right */}
                {rightElements.map((element, index) => (
                    <div key={index} className="ml-3 me-3">{element}</div>
                ))}
            </div>
        </>) : (props.children)}

    </Container>
}