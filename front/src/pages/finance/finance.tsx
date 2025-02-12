import React from "react";
import { Tab } from "react-bootstrap";

import {useSectionTabHandler} from "../../utils/routeUtils";
import {WorkspaceLayout} from "../../workspace/WorkspaceLayout";
import {Workspace} from "../../workspace/Workspace";
import {TransactionGrid} from "./transaction-grid";
import {FinanceEventKey} from "./FinanceEventKey";
import {SalaryGrid} from "./salary-grid";
import {CategoryGrid} from "./category-grid";

type FtpProps = {};

/**
 * Враппер для подсистемы FTP.
 *
 * Собирает экранные формы в табы для того, чтобы при переключении между разделами
 * react не перемонтировал компоненты
 * (в результате перемонтирования компоненты создаются заново, а состояние окон/форм сбрасывается)
 * @see header.tsx
 * @see ftp-header.tsx
 * @constructor
 */
export const Finance = (props: FtpProps) => {
    const [key, setKey] = useSectionTabHandler(
        (keyValue) => !!FinanceEventKey.getByValue(keyValue)
    );

    return (
        <Tab.Container
            defaultActiveKey={FinanceEventKey.REPORTS.value}
            activeKey={key}
        >
            <Tab.Content>

                <Tab.Pane id="operations" eventKey={FinanceEventKey.REPORTS.value}>
                    <WorkspaceLayout titleName={"Операции"}>
                        <Workspace>
                            <TransactionGrid />
                        </Workspace>
                    </WorkspaceLayout>
                </Tab.Pane>

                <Tab.Pane
                    id="salary"
                    eventKey={FinanceEventKey.SALARY.value}
                >
                    <WorkspaceLayout
                        titleName={"Заработная плата"}
                    >
                        <Workspace>
                            <SalaryGrid />
                        </Workspace>
                    </WorkspaceLayout>
                </Tab.Pane>

                <Tab.Pane
                    id="categories"
                    eventKey={FinanceEventKey.CATEGORIES.value}
                >
                    <WorkspaceLayout
                        titleName={"Категории"}
                    >
                        <Workspace>
                            <CategoryGrid />
                        </Workspace>
                    </WorkspaceLayout>
                </Tab.Pane>

                {/*<Tab.Pane id="option-gs" eventKey={FtpEventKey.OPTION_GS.value}>*/}
                {/*    <WorkspaceLayout titleName={"Опциональности"}>*/}
                {/*        <Workspace>*/}
                {/*            <OptionsGsGrid />*/}
                {/*        </Workspace>*/}
                {/*    </WorkspaceLayout>*/}
                {/*</Tab.Pane>*/}
            </Tab.Content>
        </Tab.Container>
    );
};
