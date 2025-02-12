import React from "react";
import { Tab } from "react-bootstrap";

import {useSectionTabHandler} from "../../utils/routeUtils";
import {WorkspaceLayout} from "../../workspace/WorkspaceLayout";
import {Workspace} from "../../workspace/Workspace";
import {DiagramsEventKey} from "./DiagramsEventKey";
import {DischargeDiagramsGrid} from "./discharge-diagrams-grid";
import {SalaryGrid} from "../finance/salary-grid";
import {FinanceEventKey} from "../finance/FinanceEventKey";
import {GainDiagramsGrid} from "./gain-diagrams-grid";

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
export const Diagrams = (props: FtpProps) => {
    const [key, setKey] = useSectionTabHandler(
        (keyValue) => !!DiagramsEventKey.getByValue(keyValue)
    );

    return (
        <Tab.Container
            defaultActiveKey={DiagramsEventKey.DISCHARGE.value}
            activeKey={key}
            mountOnEnter={true}
            unmountOnExit={false}
        >
            <Tab.Content>

                <Tab.Pane id="discharge-diagrams"
                          eventKey={DiagramsEventKey.DISCHARGE.value}>
                    <WorkspaceLayout
                        titleName={"Диаграммы расходов"}
                    >
                        <Workspace>
                            <DischargeDiagramsGrid />
                        </Workspace>
                    </WorkspaceLayout>
                </Tab.Pane>

                <Tab.Pane id="gain-diagrams"
                          eventKey={DiagramsEventKey.GAIN.value}
                >
                    <WorkspaceLayout
                        titleName={"Диаграммы доходов"}
                    >
                        <Workspace>
                            <GainDiagramsGrid />
                        </Workspace>
                    </WorkspaceLayout>
                </Tab.Pane>
            </Tab.Content>
        </Tab.Container>
    );
};
