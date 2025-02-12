import AlmModalDialog from "./alm-modal-dialog";
import {ListGroup, Modal, ModalProps} from "react-bootstrap";
import {Button} from "devextreme-react";
import React from "react";
import NiceModal, {useModal} from "@ebay/nice-modal-react";

type InfoModalWindowProps = {
    modalProps: ModalProps;
    title: string;
    bodyMessage: string
    bodyDetails?: string[]
}

/*
    nice-modal-react use cases: https://opensource.ebay.com/nice-modal-react/#real
    about nice-modal: https://tech.ebayinc.com/engineering/rethink-modals-management-in-react/
    nice-modal github: https://github.com/eBay/nice-modal-react
 */
export const InfoModalWindow = NiceModal.create((props: InfoModalWindowProps) => {

    const modal = useModal();

    return <AlmModalDialog {...props.modalProps} show={modal.visible} size={"xl"} onHide={() => {
        modal.resolve()
        modal.hide();
    }}>
        <Modal.Header closeButton>
            <Modal.Title>{props.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="mx-0 px-4 w-100">
            <div className="text-content">
                {props.bodyMessage}
            </div>
            {props.bodyDetails
                ? <>
                    <hr/>
                    <ListGroup className="list-group" variant="flush">
                        {
                            props.bodyDetails
                                .map((detail, idx) => (
                                    <ListGroup.Item key={idx} className="padding">
                                        {detail}
                                    </ListGroup.Item>
                                ))
                        }
                    </ListGroup>
                </>
                : null}
        </Modal.Body>
        <Modal.Footer>
            <Button text="Закрыть" stylingMode="contained" type="danger"
                    onClick={() => {
                        modal.resolve();
                        modal.hide();
                    }}/>
        </Modal.Footer>
    </AlmModalDialog>
});

export const showInfoModal = (body: string, details?: string[], title: string = "Результат", doAfter?: () => void) => {
    NiceModal.show(InfoModalWindow, {
        modalProps: {},
        title: title,
        bodyMessage: body,
        bodyDetails: details
    })
        .then(() => doAfter?.())
}


export default InfoModalWindow;