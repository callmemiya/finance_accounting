import {addDays, isAfter, isBefore, isSameDay, startOfToday, startOfTomorrow, startOfYesterday, subDays} from "date-fns";


/** Возвращает текущую дату */
export const today = (): Date =>  startOfToday();

/** Возвращает вчерашнюю дату */
export const yesterday = (): Date => startOfYesterday();

/** Возвращает позавчерашнюю дату */
export const dayBeforeYesterday = (): Date => subDays(today(), 2);

/** Возвращает завтрашнюю дату */
export const tomorrow = (): Date => startOfTomorrow();

/** Возвращает послезавтрашнюю дату */
export const dayAfterTomorrow = (): Date => addDays(today(), 2);

export const startOfMonth = (): Date => new Date(today().getFullYear(), today().getMonth(), 1, 0, 0, 0)

export const addHours = (date: Date, hours: number): Date => {
    date.setHours(date.getHours() + hours);
    return date;
}

export const dateToServerString = (date: Date): string => {
    return addHours(date, 3).toISOString();
}
