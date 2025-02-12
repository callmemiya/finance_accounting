/**
 * Сообщение об ошибке от сервера
 */
export type ApiError = {

    /** Уникальный код ошибки */
    uuid: string

    /** Пользовательское сообщение об ошибке */
    uiMessage: string

    /** Название объекта, вызвавшего ошибку */
    errorObjectName: string

    /** Техническое сообщение об ошибке */
    techMessage: string

    /** Дата и время возникновения ошибки (UTC) */
    timestamp: string

}
