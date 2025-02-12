import {TransactionDto} from "../../dto/TransactionDto";
import {startOfToday} from "date-fns";
import {useEffect, useState} from "react";
import {Column, Item, Toolbar} from "devextreme-react/data-grid";
import "../table-page.css";
import CustomDataGrid from "../../components/data-grid/CustomDataGrid";
import {Button, DateBox, NumberBox} from "devextreme-react";
import {DateTimeFormat} from "../../utils/dateTimeFormat";
import {startOfMonth, today} from "../../utils/dateUtils";
import SalaryService from "../../service/SalaryService";
import {Row} from "react-bootstrap";
import {NumberFormat} from "../../utils/numberFormat";

export type SalaryProps = {
}

export type SalaryState = {
    data?: TransactionDto[];
    sumOfTransactions?: number;
    dateFrom: Date;
    dateTo: Date;
}

const START_WITH_YEAR_NO_SECONDS = "yyyy.MM.dd HH:mm";

export const SalaryGrid = (props: SalaryProps) => {

    const service : SalaryService = new SalaryService();
    const [state, setState] = useState<SalaryState>({
        data: undefined,
        sumOfTransactions: undefined,
        dateFrom: startOfMonth(),
        dateTo: startOfToday(),
    });

    useEffect(() => {
        service
            .getSalaries(state.dateFrom, state.dateTo)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: response,
                    sumOfTransactions: response.map(transaction => transaction.operationAmount)
                        .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
                }))
            })
    }, [])

    const load = () => {
        service
            .getSalaries(state.dateFrom, state.dateTo)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: response,
                    sumOfTransactions: response.map(transaction => transaction.operationAmount)
                        .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
                }))
            })
    }

    const onDateFromChanged = (dateFrom: any) => {
        setState(prevState => ({...prevState, dateFrom: dateFrom.value}));
    }

    const onDateToChanged = (dateTo: any) => {
        setState(prevState => ({...prevState, dateTo: dateTo.value}));
    }

    return <>
        <CustomDataGrid
            dataSource={state.data}
            className={"sul-padding filter-icon"}
        >
            <Toolbar>
                <Item location="before">
                    <DateBox value={state.dateFrom}
                             label="Дата и время с"
                             displayFormat={DateTimeFormat.STANDARD_DATE_TIME}
                             width={200}
                             onValueChanged={onDateFromChanged}
                             useMaskBehavior={true}
                             onEnterKey={load}
                             type="datetime"/>
                </Item>
                <Item location="before">
                    <DateBox value={state.dateTo}
                             label="Дата и время по"
                             displayFormat={DateTimeFormat.STANDARD_DATE_TIME}
                             width={200}
                             onValueChanged={onDateToChanged}
                             useMaskBehavior={true}
                             onEnterKey={load}
                             type="datetime"/>
                </Item>
                <Item location="before">
                    <Button
                        width={200}
                        text="Обновить"
                        type="danger"
                        stylingMode="contained"
                        onClick={load}/>
                </Item>
            </Toolbar>
            <Column caption="ID" dataField="id"/>
            <Column caption="Дата операции" dataField="transactionDate" dataType="date" format={DateTimeFormat.STANDARD_DATE}/>
            <Column caption="Номер карты" dataField="cardNumber"/>
            <Column caption="Статус" dataField="status"/>
            <Column caption="Сумма" dataField="operationAmount"/>
            <Column caption="Валюта" dataField="currency"/>
            <Column caption="Кэшбек" dataField="cashback"/>
            <Column caption="Категория" dataField="category"/>
            <Column caption="Описание" dataField="description"/>
            <Column caption="Дата создания" dataField="createdDatetime" dataType="date" format={START_WITH_YEAR_NO_SECONDS}/>
            <Column caption="Дата изменения" dataField="modifiedDatetime" dataType="date" format={START_WITH_YEAR_NO_SECONDS}/>
        </CustomDataGrid>
        <Row style={{width: 550, paddingLeft: 10}}
             className="ms-1 ps-0 border-top border-bottom py-1">
            <div style={{width: 250}} className="pt-2 text-style">
                {"Сумма транзакций за отчётный период"}
            </div>
            <div style={{width: 250}} className={"pt-2"}>
                {state.sumOfTransactions}
            </div>
        </Row>
    </>

}