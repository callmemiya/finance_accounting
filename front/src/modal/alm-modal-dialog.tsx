import {Modal, ModalDialog, ModalProps} from "react-bootstrap";
// import "./modal-dialog.css"
// import "./alm-modal-dialog.css"
import {Resizable} from "re-resizable";
import {useEffect, useRef, useState} from "react";
import {Draggable} from "devextreme-react";

type AlmModalDialogState = {
    minWidth?: string | number,
    minHeight?: string | number
}

const DraggableModalDialog = (props: any) => {
    return <ModalDialog {...props} contentClassName={`${props.contentClassName} alm-modal-content-movable`}>
        <Draggable handle={".modal-header"} className={"alm-modal-draggable"} boundary={".modal"}>
            {props.children}
        </Draggable>
    </ModalDialog>
};

type AlmModalDialogProps = ModalProps & {
    isNotMovable?: boolean; // можно ли изменять положение и размер окна
    minWidth?: string | number;
    minHeight?: string | number;
    defaultWidth?: string | number;
    defaultHeight?: string | number;
}
/**
 * Модальное окно со стандартными настройками
 */
const AlmModalDialog = (props: AlmModalDialogProps) => {
    const {children, ...propsWithoutChildren} = props;
    const resizeableRef = useRef<Resizable | null>(null);
    const [state, setState] = useState<AlmModalDialogState>({});
    // задаем минимальные ширину и высоту до которой можно уменьшить модальное окно
    // без этой установки форматирование ломается
    useEffect(() => {
        if (resizeableRef.current) {
            setState(() => ({
                minWidth: props.minWidth || resizeableRef.current?.resizable?.clientWidth,
                minHeight: props.minHeight || resizeableRef.current?.resizable?.clientHeight
            }))
        }
    }, [resizeableRef.current])

    const actualProps: ModalProps = {
        ...{
            /** Признак, закрывать ли модальное окно по нажатию на ESC */
            keyboard: false,
            /** Признак, закрывать ли модальное окно по нажатию на задний фон */
            backdrop: "static",
            /** Стиль, задающий положение модального окна на экране и скругление углов */
            contentClassName: "alm-modal-content",
            /** Положение модального окна по центру */
            centered: true
        },
        ...propsWithoutChildren
    };

    return props.isNotMovable
        ? <Modal {...actualProps}>{children}</Modal>
        : <Modal {...actualProps} dialogAs={DraggableModalDialog}>
            <Resizable className={"alm-modal-resizable"}
                       enable={{
                           top: false,
                           right: true,
                           bottom: true,
                           left: false,
                           topRight: false,
                           bottomRight: true,
                           bottomLeft: false,
                           topLeft: false
                       }}
                       ref={resizeableRef}
                       defaultSize={{width: props.defaultWidth ? props.defaultWidth : "auto", height: props.defaultHeight ? props.defaultHeight : "auto"}}
                       minWidth={state.minWidth}
                       minHeight={state.minHeight}
                       maxHeight={window.innerHeight}
                       maxWidth={window.innerWidth}
            >
                {children}
            </Resizable>
        </Modal>
};

export default AlmModalDialog;
