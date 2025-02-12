import axios, {AxiosError} from "axios";
import {showInfoModal} from "../modal/info-modal-window";
import {notifyMessage} from "../notification/notification";
import {AlmResponseStatus} from "../dto/AlmResponseStatus";
import {ApiError} from "../dto/ApiError";
import {AlmResponse} from "../dto/AlmResponse";

/** Текст ошибки запроса */
const MSG_REQUEST_ERROR = 'Возникла ошибка при отправке запроса';
/** Текст ошибки запроса с описанием */
const MSG_REQUEST_ERROR_DETAILS = MSG_REQUEST_ERROR + ': ';
/** Текст ошибки в случае, если сервер недоступен */
const MSG_SERVER_UNAVAILABLE = 'Сервер недоступен';
/** Техническое сообщение о недоступности сервера */
const NETWORK_ERROR = 'Network Error';

export namespace AssertUtils {

    // TypeScript cannot use arrowFunctions for assertions.
    export function assertIsDefined<T>(value: T): asserts value is NonNullable<T> {
        if (value === undefined || value === null) {
            throw new Error(`${value} is not defined`)
        }
    }
}

export const handleBusinessError = (response: any): any => {
    if (AlmResponseStatus.BUSINESS_ERROR.name === response.status) {
        AssertUtils.assertIsDefined(response.error?.displayMessage);
        showInfoModal(response.error?.displayMessage, response.error?.details);
    }
    return response;
}

/**
 * Обработчик ошибок запроса. Если установлен флаг <b>notification</b>, и имеются данные об ошибке запроса,
 * то показывает уведомление для пользователя
 *
 * @param error        ошибка запроса
 * @param notification флаг, отображать ли уведомление для пользователя
 *
 * @see https://axios-http.com/docs/handling_errors
 * @author Vlad Radionov
 */
export const errorHandler = (error: Error | AxiosError, notification: boolean = true) => {
    _debug(error);

    if (axios.isAxiosError(error)) {
        if (error.response) {
            if (error.message === NETWORK_ERROR) {
                notify(MSG_SERVER_UNAVAILABLE, notification);
                return;
            } else if (error.response.data) {
                let response = error.response.data;
                if (AlmResponseStatus.ERROR.name === (response as AlmResponse<any>).status) {
                    handleAlmResponseError(error.response.data as AlmResponse<any>, notification)
                    return;
                }
                handleApiError(response as ApiError, notification);
                return;
            }
        }
        if (typeof error?.request === 'string') {
            console.error(MSG_REQUEST_ERROR_DETAILS, error.request);
        }
    } else {
        if (error.message) {
            console.error(MSG_REQUEST_ERROR_DETAILS, error.message);
        }
    }

    notify(MSG_REQUEST_ERROR, notification);
}

/**
 * Выполняет обработку ошибки, возвращенной сервером
 *
 * @param apiError     ошибка, полученная с сервера
 * @param notification флаг, отображать ли уведомление для пользователя
 */
const handleApiError = (apiError: ApiError, notification: boolean) => {
    if (apiError.uiMessage) {
        if (apiError.errorObjectName) {
            notify(MSG_REQUEST_ERROR_DETAILS + apiError.uiMessage + " \"" + apiError.errorObjectName + "\"", notification);
        } else {
            notify(MSG_REQUEST_ERROR_DETAILS + apiError.uiMessage, notification);
        }
    } else {
        notify(MSG_REQUEST_ERROR, notification);
    }
    if (apiError.techMessage) {
        console.error(MSG_REQUEST_ERROR_DETAILS, apiError.techMessage);
    }
}

const handleAlmResponseError = (almResponse: AlmResponse<any>, notification: boolean) => {
    if (AlmResponseStatus.ERROR.name === almResponse.status) {
        AssertUtils.assertIsDefined(almResponse.error?.displayMessage);
        AssertUtils.assertIsDefined(almResponse.error?.message);
        notify(`${MSG_REQUEST_ERROR_DETAILS}${almResponse.error?.displayMessage}`, notification)
        console.error(MSG_REQUEST_ERROR_DETAILS, almResponse.error?.message);
    }
}

/**
 * Отображает уведомление для пользователя с указанным сообщением или журналирует его в консоль
 *
 * @param msg  сообщение уведомления
 * @param show показывать ли уведомление для пользователя
 */
const notify = (msg: string, show: boolean) => show ? notifyMessage(msg) : console.error(msg);

/** Раскомментировать для журналирования доступных данных в ошибке */
const _debug = (_error: Error | AxiosError) => {
    // if (axios.isAxiosError(_error)) {
    //     console.debug('Axios Error:', _error);
    //     console.debug('error.response:', _error.response);
    //     console.debug('error.response.status:', _error?.response?.status);
    //     console.debug('error.response.statusText:', _error?.response?.statusText);
    //     console.debug('error.response.headers:', _error?.response?.headers);
    //     console.debug('error.response.data:', _error.response?.data);
    //     console.debug('error.request: ', _error.request);
    //     console.debug('error.message: ', _error.message);
    // } else {
    //     console.debug('Stock Error:', _error);
    //     console.debug('error.name:', _error.name);
    //     console.debug('error.message:', _error.message);
    //     console.debug('error.cause:', _error.cause);
    //     console.debug('error.stack:', _error.stack);
    // }
}
