/**
 * DTO для сущности транзакции
 */
export interface TransactionDto {

    id: number;
    transactionDate: Date;
    cardNumber: string;
    status: string;
    operationAmount: number;
    currency: string;
    cashback: number;
    category: string
    description: string;
    createdDatetime: Date;
    modifiedDatetime: Date;

}