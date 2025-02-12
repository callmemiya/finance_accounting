/**
 * Формат даты и времени для справочников
 */
export const DateTimeFormat = {

    /**
     * Стандартный формат даты и времени
     *
     * @example 03.02.2024 12:10:33
     */
    STANDARD_DATE_TIME_SECONDS: "dd.MM.yyyy HH:mm:ss",

    /**
     * Стандартный формат даты и времени, без секунд
     *
     * @example 03.02.2024 12:10
     */
    STANDARD_DATE_TIME: "dd.MM.yyyy HH:mm",

    /**
     * Стандартный формат даты
     *
     * @example 03.02.2024
     */
    STANDARD_DATE: "dd.MM.yyyy"

} as const

export type DateTimeFormat = typeof DateTimeFormat[keyof typeof DateTimeFormat];
