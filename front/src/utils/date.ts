import {formatISO} from "date-fns";
/**
 * Преобразует дату в строку в формате ISO для сервера. При отсутствии значения возвращает undefined
 *
 * @example 2024-02-03
 */

export function formatDateForServer(date?: string | Date | null): string | undefined {
    return date ? formatISO(date, {representation: 'date'}) : undefined;
}