import React, {JSXElementConstructor} from "react";
import {Button, DataGrid} from "devextreme-react";
import {IToolbarProps as TreeGridToolbarProps, Toolbar, TreeList} from "devextreme-react/tree-list";
import {Item, IToolbarProps as DataGridToolbarProps} from "devextreme-react/data-grid";
import {EditorPreparingEvent} from "devextreme/ui/data_grid";


export const getChild = (children: React.ReactNode, type: string | JSXElementConstructor<any>) => {
    if (Array.isArray(children)) {
        return children.find(child => React.isValidElement(child) && child.type === type)
    } else if (React.isValidElement(children) && children.type === type) {
        return children;
    }
}

export const excludeChild = (children: React.ReactNode, type: string | JSXElementConstructor<any>) => {
    if (Array.isArray(children)) {
        return children.filter(child => !(React.isValidElement(child) && child?.type == type)); // filter children array for requested element
    } else if (React.isValidElement(children) && children.type === type) {
        return []; // children is a 1 request element -> return empty array
    } else {
        return children; // nothing to exclude request child not found
    }
}

export const searchPanelExists = (toolbar: any) => {
    const toolbarItems = (toolbar?.props?.children as Item[])
    if (toolbarItems?.length > 0) {
        const searchPanel = toolbarItems?.filter(item => {
            if (item.props && item.props.name === "searchPanel") {
                return item;
            }
        });
        if (searchPanel && searchPanel.length > 0) return true;
    }
    return false;
}

export const configureToolbar = (children: any[],
                                 Toolbar: React.ComponentType<DataGridToolbarProps | TreeGridToolbarProps>,
                                 toolbarKey: string,
                                 searchPanelRequired?: boolean,
                                 columnChooserRequired?: boolean,
                                 stateStoringRequired?: boolean,
                                 gridRef?: React.RefObject<DataGrid> | React.MutableRefObject<DataGrid> | React.RefObject<TreeList>): any[] => {
    if (searchPanelRequired || columnChooserRequired || stateStoringRequired) {
        const toolbar = getChild(children, Toolbar);
        const {children: toolbarChildren, ...restProps} = toolbar?.props ?? {};

        searchPanelRequired = searchPanelExists(toolbar) ? false : searchPanelRequired;

        const toolbarWithDefaults = <Toolbar key={toolbarKey} {...restProps}>
            {toolbarChildren}
            {searchPanelRequired ? <Item name="searchPanel" locateInMenu={"auto"}/> : null}
            {columnChooserRequired ? <Item name="columnChooserButton" locateInMenu={"auto"}/> : null}
            {/*К тулбару добавляем кнопку сброса параметров*/}
            {getRevertStateButton(stateStoringRequired, gridRef)}
        </Toolbar>
        return [excludeChild(children, Toolbar), toolbarWithDefaults]
    }
    return children;
}

export const getRevertStateButton = (isRequired?: boolean, gridRef?: React.RefObject<DataGrid> | React.RefObject<TreeList>) => {
    return isRequired ? <Item location="after">
        <Button key={"alm-data-grid-revert-state-button"}
                icon={"revert"} stylingMode="contained"
                hint={"Восстановить положение полей"}
                onClick={() => gridRef!.current?.instance.state(null)}/>
    </Item> : undefined
}

/**
 * disable value changing by mouse scroll in specific fields
 * */
export const onEditorPreparingDisableScrolling = (e: EditorPreparingEvent, dataFields: string[]) => {
    if (!!e.dataField && dataFields.includes(e.dataField)) {
        e.editorOptions.step = 0
    }
}

export type CustomGridWithoutStateStoring = {
    /** Нужно ли хранить настройки грида пользователя */
    stateStoringRequired?: false;
}

export type CustomGridWithStateStoring = {
    /** Нужно ли хранить настройки грида пользователя */
    stateStoringRequired: true;
    /** id - уникальный идентификатор грида. id и userInfo.uid вместе составляют ключ для хранения настроек грида пользователя */
    id: string;
}