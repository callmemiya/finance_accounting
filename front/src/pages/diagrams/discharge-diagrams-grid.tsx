import {TransactionDto} from "../../dto/TransactionDto";

import "../table-page.css";
import PieChart, {
    Series,
    Label,
    Margin,
    Legend,
    Animation, Tooltip, HoverStyle,
} from 'devextreme-react/pie-chart';
import {useEffect, useState} from "react";
import {startOfMonth} from "../../utils/dateUtils";
import {startOfToday} from "date-fns";
import {AlmToolbar} from "../../components/toolbar/AlmToolbar";
import {Button, DateBox} from "devextreme-react";
import {DateTimeFormat} from "../../utils/dateTimeFormat";
import {Row} from "react-bootstrap";
import TransactionService from "../../service/TransactionService";

export type DischargeDiagramsProps = {
}

type CategoryGroupPie = {
    category: string,
    amount: number
}

export type DischargeDiagramsState = {
    data?: CategoryGroupPie[];
    dateFrom: Date;
    dateTo: Date;
}

function formatText(arg: { argumentText: string; percentText: string; }) {
    return `${arg.argumentText} (${arg.percentText})`;
}

const groupByCategory = (transactions: TransactionDto[]) => {
    const grouped = transactions?.reduce((acc, transaction) => {
        if (acc[transaction.category]) {
            acc[transaction.category].amount += transaction.operationAmount;
        } else {
            acc[transaction.category] = {
                category: transaction.category,
                amount: transaction.operationAmount,
            };
        }
        return acc;
    }, {} as { [key: string]: { category: string; amount: number } });

    return Object.values(grouped);
};

export const DischargeDiagramsGrid = (props: DischargeDiagramsProps) => {

    const service : TransactionService = new TransactionService();
    const [state, setState] = useState<DischargeDiagramsState>({
        data: undefined,
        dateFrom: startOfMonth(),
        dateTo: startOfToday(),
    });

    useEffect(() => {
        service
            .getDischarges(state.dateFrom, state.dateTo)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: groupByCategory(response),
                }))
            })
    }, [])

    const load = () => {
        service
            .getDischarges(state.dateFrom, state.dateTo)
            .then(response => {
                setState(prevState => ({
                    ...prevState,
                    data: groupByCategory(response),
                }))
            })
    }

    const onDateFromChanged = (dateFrom: any) => {
        setState(prevState => ({...prevState, dateFrom: dateFrom.value}));
    }

    const onDateToChanged = (dateTo: any) => {
        setState(prevState => ({...prevState, dateTo: dateTo.value}));
    }

    const customizeTooltip = (info: any) => {
        const formattedAmount = Number(info.valueText).toFixed(2);
        return {
            text: `${info.argumentText}: ${formattedAmount}`
        };
    };

    return <>
        <AlmToolbar>
            <Row>
                <DateBox value={state.dateFrom}
                         label="Дата и время с"
                         displayFormat={DateTimeFormat.STANDARD_DATE_TIME}
                         width={200}
                         onValueChanged={onDateFromChanged}
                         useMaskBehavior={true}
                         onEnterKey={load}
                         type="datetime"/>
                <DateBox value={state.dateTo}
                         label="Дата и время по"
                         displayFormat={DateTimeFormat.STANDARD_DATE_TIME}
                         width={200}
                         onValueChanged={onDateToChanged}
                         useMaskBehavior={true}
                         onEnterKey={load}
                         type="datetime"/>
                <Button
                    width={200}
                    text="Обновить"
                    type="danger"
                    stylingMode="contained"
                    onClick={load}/>
            </Row>
        </AlmToolbar>
        <PieChart
            id="discharge-diagram-pie"
            type={"donut"}
            innerRadius={0.7}
            dataSource={state.data}
            palette="Bright"
            title="Распределение расходов по категориям"
            resolveLabelOverlapping={"shift"}
        >
            <Series
                argumentField="category"
                valueField="amount"
                hoverStyle={{
                    border: {
                        width: 7,
                        visible: true,
                        dashStyle: "solid"
                    },
                    hatching: {
                        direction: 'right',
                        step: 5,
                        width: 5,
                        opacity: 0.7,
                    }
                }}
            >
                <Label visible={true} customizeText={formatText} />
            </Series>
            <Legend visible={false} />
            <Animation enabled={true} />
            <Tooltip
                enabled={true}
                customizeTooltip={customizeTooltip}
                cornerRadius={15}
            />
        </PieChart>
    </>

}