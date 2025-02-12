import {getResource} from "../utils/axiosApi";
import {TransactionDto} from "../dto/TransactionDto";
import {dateToServerString} from "../utils/dateUtils";

/**
 * Сервис для работы с api
 */
export default class SalaryService {

    private static BASE_URL = "/salary";

    getSalaries(dateFrom: Date, dateTo: Date): Promise<TransactionDto[]> {
        return getResource(SalaryService.BASE_URL, {
            params: {
                dateFrom: dateToServerString(dateFrom),
                dateTo: dateToServerString(dateTo)
            }
        });
    }

}