import {getResource} from "../utils/axiosApi";
import {TransactionDto} from "../dto/TransactionDto";
import {CategoryDto} from "../dto/CategoryDto";
import {dateToServerString} from "../utils/dateUtils";

/**
 * Сервис для работы с api
 */
export default class CategoryService {

    private static BASE_URL = "/category";

    getCategories(dateFrom: Date, dateTo: Date): Promise<CategoryDto[]> {
        return getResource(CategoryService.BASE_URL, {
            params: {
                dateFrom: dateToServerString(dateFrom),
                dateTo: dateToServerString(dateTo)
            }
        });
    }

    getAllCategories(): Promise<CategoryDto[]> {
        return getResource(CategoryService.BASE_URL + "/all");
    }

}