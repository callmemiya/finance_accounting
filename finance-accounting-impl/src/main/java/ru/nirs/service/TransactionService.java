package ru.nirs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.nirs.dao.AccountDao;
import ru.nirs.dao.CategoryDao;
import ru.nirs.dao.TransactionDao;
import ru.nirs.entity.Transaction;
import ru.nirs.entity.TransactionProjection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Сервис для работы с транзакциями
 */
@Service
@Validated
public class TransactionService {

    private static final String ERROR_TRANSACTION_NOT_FOUND = "Transaction with id = %s doesn't exist";
    private static final String ERROR_INSUFFICIENT_TRANSACTIONS = "At least 2 transactions are required for joining";
    private static final String ERROR_CATEGORY_NOT_FOUND = "Category with id = %s doesn't exist";
    private static final String ERROR_ACCOUNT_NOT_FOUND = "Account 'WITHOUT_CARD' not found";

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AccountDao accountDao;

    public List<TransactionProjection> getAll(LocalDateTime dateFrom, LocalDateTime dateTo, Long categoryId) {
        return transactionDao.getAllProjections(dateFrom, dateTo, categoryId);
    }

    public List<TransactionProjection> getDischarges(LocalDateTime dateFrom, LocalDateTime dateTo, Long categoryId) {
        return transactionDao.getDischarges(dateFrom, dateTo, categoryId);
    }

    public List<TransactionProjection> getGains(LocalDateTime dateFrom, LocalDateTime dateTo, Long categoryId) {
        return transactionDao.getGains(dateFrom, dateTo, categoryId);
    }

    public List<TransactionProjection> getSalaries(LocalDateTime dateFrom, LocalDateTime dateTo) {
        var categoryId = categoryDao.getByCategoryName("Зарплата").orElseThrow().getId();
        return transactionDao.getSalaries(dateFrom, dateTo, categoryId);
    }

    public Long create(@Valid @NotNull Transaction entity) {
        return transactionDao.save(entity.toBuilder()
                        .id(null)
                        .createdDatetime(LocalDateTime.now())
                        .modifiedDatetime(LocalDateTime.now())
                        .build())
                .getId();
    }

    public void update(@Valid Transaction transaction) {
        transactionDao.findById(transaction.getId())
                .map(transactionSaved -> transactionSaved.toBuilder()
                        .modifiedDatetime(LocalDateTime.now())
                        .build())
                .map(transactionDao::save)
                .orElseThrow(
                        () -> new IllegalArgumentException(format(ERROR_TRANSACTION_NOT_FOUND, transaction.getId())));
    }

    public Long delete(@NotNull Long id) {
        return transactionDao.deleteByTransactionId(id);
    }

    @Transactional
    public void joinTransactions(List<Long> transactionIds, Long categoryId, String description) {
        if (transactionIds.size() < 2) {
            throw new IllegalArgumentException(ERROR_INSUFFICIENT_TRANSACTIONS);
        }

        // Проверяем существование категории
        categoryDao.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(format(ERROR_CATEGORY_NOT_FOUND, categoryId)));

        // Получаем все транзакции для объединения
        List<Transaction> transactions = transactionIds.stream()
                .map(id -> transactionDao.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(format(ERROR_TRANSACTION_NOT_FOUND, id))))
                .collect(Collectors.toList());

        // Вычисляем общие суммы
        double totalAmount = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        double totalCashback = transactions.stream()
                .mapToDouble(t -> t.getCashback() != null ? t.getCashback() : 0.0)
                .sum();

        // Определяем accountId
        Long accountId;
        boolean allAccountsAreSame = transactions.stream()
                .map(Transaction::getAccountId)
                .distinct()
                .count() == 1;

        if (allAccountsAreSame) {
            accountId = transactions.get(0).getAccountId();
        } else {
            accountId = accountDao.getByAccountName("WITHOUT_CARD")
                    .orElseThrow(() -> new IllegalArgumentException(ERROR_ACCOUNT_NOT_FOUND))
                    .getId();
        }

        // Берем данные из первой транзакции для новой
        Transaction firstTransaction = transactions.get(0);
        
        // Создаем новую транзакцию
        Transaction newTransaction = Transaction.builder()
                .accountId(accountId)
                .categoryId(categoryId)
                .status(firstTransaction.getStatus())
                .amount(totalAmount)
                .cashback(totalCashback)
                .transactionDate(firstTransaction.getTransactionDate())
                .description(description)
                .createdDatetime(LocalDateTime.now())
                .modifiedDatetime(LocalDateTime.now())
                .build();

        // Сохраняем новую транзакцию
        transactionDao.save(newTransaction);

        // Удаляем исходные транзакции
        transactionDao.updateIsJoinedByTransactionId(true, transactionIds);
    }
}
