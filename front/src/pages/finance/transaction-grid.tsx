import {TransactionDto} from "../../dto/TransactionDto";
import TransactionService from "../../service/TransactionService";
import {useEffect, useState} from "react";
import {Column, Item, Toolbar, Selection} from "devextreme-react/data-grid";
import "../table-page.css";
import {Workspace} from "../../workspace/Workspace";
import CustomDataGrid from "../../components/data-grid/CustomDataGrid";
import {Button, DateBox, SelectBox} from "devextreme-react";
import {DateTimeFormat} from "../../utils/dateTimeFormat";
import {startOfMonth, today} from "../../utils/dateUtils";
import CategoryService from "../../service/CategoryService";
import {CategoryDto} from "../../dto/CategoryDto";
import {startOfDay, startOfToday} from "date-fns";
import {Row} from "react-bootstrap";
import {JoinTransactionsModal} from "./join-transactions-modal";

export type TransactionProps = {
}

export type TransactionState = {
    data?: TransactionDto[];
    dateFrom: Date;
    dateTo: Date;
    category?: number;
    categories: CategoryDto[];
    sumOfTransactions?: number;
    isJoinMode: boolean;
    selectedTransactionIds: number[];
    showJoinModal: boolean;
}

const START_WITH_YEAR_NO_SECONDS = "yyyy.MM.dd HH:mm";

export const TransactionGrid = (props: TransactionProps) => {

    const service : TransactionService = new TransactionService();
    const categoryService : CategoryService = new CategoryService();
    const [state, setState] = useState<TransactionState>({
        data: undefined,
        dateFrom: startOfMonth(),
        dateTo: startOfDay(new Date()),
        category: undefined,
        categories: [],
        sumOfTransactions: undefined,
        isJoinMode: false,
        selectedTransactionIds: [],
        showJoinModal: false
    });

    useEffect(() => {
        service
            .getTransactionProjections(state.dateFrom, state.dateTo, state.category)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: response,
                    sumOfTransactions: response.filter(transaction => transaction.status === "OK")
                        .map(transaction => transaction.operationAmount)
                        .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
                }))
            })
        categoryService
            .getCategories(state.dateFrom, state.dateTo)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    categories: response.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
                }))
            })
    }, [])

    useEffect(() => {
        categoryService
            .getCategories(state.dateFrom, state.dateTo)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    categories: response.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
                }))
            })
    }, [state.dateFrom, state.dateTo])

    const load = () => {
        service
            .getTransactionProjections(state.dateFrom, state.dateTo, state.category)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: response,
                    sumOfTransactions: response.filter(transaction => transaction.status === "OK")
                        .map(transaction => transaction.operationAmount)
                        .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
                }))
            })
        categoryService
            .getCategories(state.dateFrom, state.dateTo)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    categories: response.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
                }))
            })
    }

    const onDateFromChanged = (dateFrom: any) => {
        setState(prevState => ({...prevState, dateFrom: dateFrom.value}));
    }

    const onDateToChanged = (dateTo: any) => {
        setState(prevState => ({...prevState, dateTo: dateTo.value}));
    }

    const onCategoryChanged = (category: any) => {
        setState(prevState => ({...prevState, category: category}));
    }

    const toggleJoinMode = () => {
        setState(prevState => ({
            ...prevState,
            isJoinMode: !prevState.isJoinMode,
            selectedTransactionIds: [],
            showJoinModal: false
        }));
    }

    const onSelectionChanged = (e: any) => {
        setState(prevState => ({
            ...prevState,
            selectedTransactionIds: e.selectedRowKeys
        }));
    }

    const handleJoinClick = () => {
        if (state.selectedTransactionIds.length < 2) {
            alert('Пожалуйста, выберите минимум 2 транзакции для объединения');
            return;
        }
        setState(prevState => ({...prevState, showJoinModal: true}));
    }

    const handleJoinModalHide = () => {
        setState(prevState => ({...prevState, showJoinModal: false}));
    }

    const handleJoinModalSave = async (categoryId: number, description: string) => {
        try {
            await service.joinTransactions(state.selectedTransactionIds, categoryId, description);
            setState(prevState => ({
                ...prevState,
                isJoinMode: false,
                selectedTransactionIds: [],
                showJoinModal: false
            }));
            load(); // Перезагружаем данные после объединения
        } catch (error) {
            alert('Произошла ошибка при объединении транзакций');
        }
    }

    const getSelectedTransactions = (): TransactionDto[] => {
        if (!state.data) return [];
        return state.data.filter(transaction => 
            state.selectedTransactionIds.includes(transaction.id)
        );
    }

    return <>
        <CustomDataGrid
            dataSource={state.data}
            className={"sul-padding filter-icon"}
            selection={{ mode: state.isJoinMode ? 'multiple' : 'none' }}
            onSelectionChanged={onSelectionChanged}
            keyExpr="id"
        >
            <Selection mode={state.isJoinMode ? 'multiple' : 'none'} />
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
                    <SelectBox value={state.category}
                               label="Категория"
                               showClearButton={true}
                               displayExpr="categoryName"
                               valueExpr="id"
                               dataSource={state.categories}
                               onValueChanged={e => onCategoryChanged(e.value)}
                    />
                </Item>
                <Item location="before">
                    <Button
                        width={200}
                        text="Обновить"
                        type="danger"
                        stylingMode="contained"
                        onClick={load}/>
                </Item>
                <Item location="before">
                    <Button
                        width={200}
                        text="Объединение"
                        type={state.isJoinMode ? "success" : "normal"}
                        stylingMode="contained"
                        onClick={toggleJoinMode}/>
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
        <Row style={{width: 600, paddingLeft: 10}}
             className="ms-1 ps-0 border-top border-bottom py-1">
            <div style={{width: 350}} className="pt-2 text-style">
                {"Сумма транзакций за отчётный период"}
            </div>
            <div style={{width: 250}} className={"pt-2"}>
                {state.sumOfTransactions?.toFixed(2)}
            </div>
        </Row>
        {state.isJoinMode && (
            <Row className="mt-3 ms-1">
                <Button
                    width={200}
                    text="Объединить"
                    type="success"
                    stylingMode="contained"
                    onClick={handleJoinClick}
                    disabled={state.selectedTransactionIds.length < 2}
                    className="me-2"
                />
                <Button
                    width={200}
                    text="Отмена"
                    type="normal"
                    stylingMode="contained"
                    onClick={toggleJoinMode}
                />
            </Row>
        )}

        <JoinTransactionsModal
            show={state.showJoinModal}
            onHide={handleJoinModalHide}
            onSave={handleJoinModalSave}
            selectedTransactions={getSelectedTransactions()}
            categories={state.categories}
        />
    </>
}