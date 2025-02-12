import {DataGrid} from "devextreme-react";
import {IDataGridOptions, Toolbar} from "devextreme-react/data-grid";
import {useRef} from "react";
import {CustomGridWithoutStateStoring, CustomGridWithStateStoring, configureToolbar} from "../../utils/gridUtils";

/**
 * Базовые свойства, которые используются при работе с таблицами, для возможности нарисовать грид с внешней
 * или внутренней панелью инструментов и нужно ли растягивать грид до максимумма доступной области
 * TODO: подумать над тем, чтобы унаследовать свойства от IDataGridOptions
 */
export type CustomDataGridBaseProps = {
    showExternalToolbar?: boolean;
    /** Нужно ли растягивать грид до максимума доступной области просмотра */
    heightToVh?: boolean;
}

export type CustomDataGridOptions = IDataGridOptions & (CustomGridWithoutStateStoring | CustomGridWithStateStoring)

/**
 * Грид со стандартными настройками
 *
 * @see https://dev.to/bytebodger/default-props-in-react-ts-part-deux-2ic3
 * @see https://blog.logrocket.com/complete-guide-react-default-props - default-props could be deprecated
 * @see https://blog.logrocket.com/the-beginners-guide-to-mastering-react-props-3f6f01fd7099
 */
const CustomDataGrid = (props: CustomDataGridOptions) => {

    const gridRef = useRef<DataGrid>(null);

    const {children: childrenFromProps, ...propsWithoutChildren} = props;
    // забираем children, чтобы можно было к ним добавить дефолтных
    let children = Array.isArray(childrenFromProps) ? [...childrenFromProps] : [childrenFromProps]
    // К Toolbar добавляем дефолтные компоненты
    children = configureToolbar(children, Toolbar, "alm-grid-toolbar",
        props.searchPanel?.visible ?? true,
        props.columnChooser?.enabled ?? true, props.stateStoringRequired, gridRef);

    const actualProps: IDataGridOptions = {
        ...{
            /** Данные */
            keyExpr: "id",
            wordWrapEnabled: false,
            /** Панель поиска */
            searchPanel: {visible: true, width: 240, searchVisibleColumnsOnly: true},
            /** Выбор отображаемых столбцов */
            height: 300,
            columnChooser: {enabled: false},
            headerFilter: {visible: false},
            scrolling: {mode: "standard"},
            paging: {enabled: false},
            /** Отображение */
            showBorders: false,
            width: '100%',
            /** Фиксированная минимальная ширина столбца, чтобы не было возможности полностью "схлопнуть" столбец */
            columnMinWidth: 75,
            /** Столбцы */
            /** https://js.devexpress.com/Documentation/Guide/UI_Components/DataGrid/Columns/Column_Types/Command_Columns/ */
            allowColumnReordering: false,
            /** Позволяет пользователям изменять размер столбцов */
            allowColumnResizing: true,
            /** https://js.devexpress.com/Documentation/Guide/UI_Components/DataGrid/Columns/Adaptability/ */
            columnHidingEnabled: false,
            columnAutoWidth: true,
            columnFixing: {enabled: true},
            columnWidth: "auto",
            showColumnLines: false,
            /** Строки */
            rowAlternationEnabled: false,
            showRowLines: true,
        },
        ...propsWithoutChildren
    };

    return <DataGrid {...actualProps} ref={gridRef} className={"flex-grow-1 ".concat(props.className ?? "")}>
        {children}
    </DataGrid>
};

export default CustomDataGrid;