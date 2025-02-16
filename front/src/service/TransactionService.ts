import {getResource, postData} from "../utils/axiosApi";
import {TransactionDto} from "../dto/TransactionDto";
import {dateToServerString} from "../utils/dateUtils";

/**
 * Сервис для работы с api
 */
export default class TransactionService {

    private static BASE_URL = "/transactions";

    getTransactionProjections(dateFrom: Date, dateTo: Date, category?: number): Promise<TransactionDto[]> {
        return getResource(TransactionService.BASE_URL, {
            params: {
                dateFrom: dateToServerString(dateFrom),
                dateTo: dateToServerString(dateTo),
                categoryId: category,
            }
        });
    }

    getDischarges(dateFrom: Date, dateTo: Date, category?: number): Promise<TransactionDto[]> {
        return getResource(TransactionService.BASE_URL + "/discharges", {
            params: {
                dateFrom: dateToServerString(dateFrom),
                dateTo: dateToServerString(dateTo),
                categoryId: category,
            }
        });
    }

    getGains(dateFrom: Date, dateTo: Date, category?: number): Promise<TransactionDto[]> {
        return getResource(TransactionService.BASE_URL + "/gains", {
            params: {
                dateFrom: dateToServerString(dateFrom),
                dateTo: dateToServerString(dateTo),
                categoryId: category,
            }
        });
    }

    joinTransactions(transactionIds: number[], categoryId: number, description: string): Promise<void> {
        return postData(TransactionService.BASE_URL + "/join", {
            transactionIds,
            categoryId,
            description
        });
    }
}