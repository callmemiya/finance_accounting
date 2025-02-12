/**
 * Формат числа
 */
export const NumberFormat = {

    /**
     * Денежный формат
     *
     * @example 1 024.20
     */
    MONEY: ",##0.00",

    /**
     * Денежный формат для вывода числа с точностью до четырех знаков после запятой
     *
     * @example 1024.2000
     */
    MONEY_FORMAT_TEN_THOUSANDTH_PRECISION: ",##0.0000",

    /**
     * Формат для значений ставок
     *
     * @example 155.125365
     */
    RATE: ",##0.000000",

    /**
     * Формат для отображения чисел с четырмя знаками после запятой
     *
     * @example 11.0000
     */
    FOUR_DECIMAL_PLACES: ",##0.0000",

    /**
     * Формат для процентных значений со знаком %
     *
     * @example 150.35%
     */
    PERCENT_WITH_SIGN: "#0.00'%'",

    /**
     * Формат для процентных значений без знака %
     *
     * @example 150.35
     */
    PERCENT_WITHOUT_SIGN: "#0.00",

} as const

export type NumberFormatType = typeof NumberFormat[keyof typeof NumberFormat];
