import axios, {AxiosRequestConfig, AxiosResponse} from "axios";
import {errorHandler, handleBusinessError} from "./errorHandler";

/** Базовый URL */
export const _API_BASE_URL = "http://localhost:8088";

/**
 * Выполняет GET-запрос по переданному URL
 *
 * @param url URL запроса
 * @param config конфигурация запроса
 * @param notification флаг, отображать ли уведомление для пользователя в случае ошибок запроса
 * @return содержимое ответа на запроса
 */
export const getResource = async <T>(url: string, config?: AxiosRequestConfig, notification: boolean = true) => {
    return handleResponse(url, axios.get(_API_BASE_URL + url, {...config}), notification);
}

/**
 * Выполняет POST-запрос по переданному URL
 *
 * @param url          URL запроса
 * @param data         тело запроса
 * @param config конфигурация запроса
 * @param notification флаг, отображать ли уведомление для пользователя в случае ошибок запроса
 * @return содержимое ответа на запрос
 */
export const postData = async (url: string, data: object = {}, config?: AxiosRequestConfig, notification: boolean = true) => {
    return handleResponse(url, axios.post(_API_BASE_URL + url, data, {...config}), notification)

};

/**
 * Выполняет PUT-запрос по переданному URL
 *
 * @param url          URL запроса
 * @param data         тело запроса
 * @param notification флаг, отображать ли уведомление для пользователя в случае ошибок запроса
 * @return содержимое ответа на запрос
 */
export const putData = async (url: string, data: object = {}, notification: boolean = true) => {
    return handleResponse(url, axios.put(_API_BASE_URL + url, data), notification);
};

/**
 * Выполняет DELETE-запрос по переданному URL
 *
 * @param url          URL запроса
 * @param data         тело запроса
 * @param notification флаг, отображать ли уведомление для пользователя в случае ошибок запроса
 * @return содержимое ответа на запрос
 */
export const deleteData = async (url: string, data: object = {}, notification: boolean = true) => {
    return handleResponse(url, axios.delete(_API_BASE_URL + url, data), notification);
};

const handleResponse = (url: string, axiosResponse: Promise<AxiosResponse>, notification: boolean = true) => {
    return axiosResponse
        .then(response => {
            //Проверяем наличие редиректа и навигируем браузер на новый URL, если требуется
            checkRedirect(url, response);
            return response.data;
        })
        .then(responseData => handleBusinessError(responseData))
        // About promise: if you have a reject handler, and it does not throw or return a rejected promise, then the chain become fulfilled again
        // https://stackoverflow.com/questions/42583589/will-reject-skip-all-following-then-in-promise
        .catch(error => errorHandler(error, notification));
}

/**
 * Выполняет навигацию браузера на страницу редиректа, если в ответе на запрос есть такой признак.
 * Признак редиректа определяется путем сравнения изначального URL запроса от URL, указанного в ответе
 *
 * @param url      изначальный URL запроса
 * @param response ответ на запрос
 *
 * @see https://stackoverflow.com/questions/55926127/does-axios-have-the-ability-to-detect-redirects
 */
const checkRedirect = (url: string, response: AxiosResponse) => {
    const encodedUrl = encodeURI(url.trimEnd());
    //Определяем, был ли редирект
    if (response?.request?.responseURL) {
        /**
         * Проверяем соответствует ли URL след. условиям:
         * - URL из запроса не содержит переданный в метод URL и не равен ему
         * - URL из запроса не содержит переданный в метод URL с кодированием спец. символов и не равен ему
         */
        if (response.request.responseURL !== url && !response.request.responseURL.includes(url)
            && response.request.responseURL !== encodedUrl && !response.request.responseURL.includes(encodedUrl)) {
            //Навигируем браузер на новый URL
            window.location.href = response.request.responseURL;
        }
    }
}
