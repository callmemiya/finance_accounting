import {useEffect, useState} from "react";
import {Column} from "devextreme-react/data-grid";
import "../table-page.css";
import CustomDataGrid from "../../components/data-grid/CustomDataGrid";
import {DateTimeFormat} from "../../utils/dateTimeFormat";
import CategoryService from "../../service/CategoryService";
import {CategoryDto} from "../../dto/CategoryDto";

export type CategoryProps = {
}

export type CategoryState = {
    data?: CategoryDto[];
}

const START_WITH_YEAR_NO_SECONDS = "yyyy.MM.dd HH:mm";

export const CategoryGrid = (props: CategoryProps) => {

    const service : CategoryService = new CategoryService();
    const [state, setState] = useState<CategoryState>({
        data: undefined,
    });

    useEffect(() => {
        service
            .getAllCategories()
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: response,
                }))
            })
    }, [])

    const load = () => {
        service
            .getAllCategories()
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: response,
                }))
            })
    }

    return <>
        <CustomDataGrid
            dataSource={state.data}
            className={"sul-padding filter-icon"}
        >
            <Column caption="ID" dataField="id" sortOrder="asc"/>
            <Column caption="Наименование категории" dataField="categoryName" dataType="string"/>
            <Column caption="Подстроки, соответствующие категории"
                    dataField="categorySubstrings"
                    calculateCellValue={(rowData: CategoryDto) => rowData.categorySubstrings != undefined ?
                        rowData.categorySubstrings.join(', ').substring(1, rowData.categorySubstrings.join(', ').length -1) : "-"}
            />
            <Column caption="Дата создания" dataField="createdDatetime" dataType="date" format={START_WITH_YEAR_NO_SECONDS}/>
            <Column caption="Дата изменения" dataField="modifiedDatetime" dataType="date" format={START_WITH_YEAR_NO_SECONDS}/>
        </CustomDataGrid>
    </>

}