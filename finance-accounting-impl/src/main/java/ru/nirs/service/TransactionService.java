package ru.nirs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.nirs.dao.CategoryDao;
import ru.nirs.dao.TransactionDao;
import ru.nirs.entity.Transaction;
import ru.nirs.entity.TransactionProjection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

/**
 * Сервис для работы с транзакциями
 */
@Service
public class TransactionService {

    private static final String ERROR_TRANSACTION_NOT_FOUND = "Transaction with id = %s doesn't exist";

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private CategoryDao categoryDao;

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

}
