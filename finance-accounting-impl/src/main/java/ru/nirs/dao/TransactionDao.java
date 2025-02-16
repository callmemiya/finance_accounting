package ru.nirs.dao;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.nirs.entity.Transaction;
import ru.nirs.entity.TransactionProjection;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с {@link Transaction}
 */
public interface TransactionDao extends CrudRepository<Transaction, Long> {

    String TRANSACTION_PROJECTIONS_QUERY = "select tr.id, " +
            "       ac.account_name card_number, " +
            "       ct.category_name category, " +
            "       tr.status, " +
            "       tr.amount operation_amount, " +
            "       ac.currency, " +
            "       tr.cashback, " +
            "       tr.transaction_date, " +
            "       tr.description, " +
            "       tr.created, " +
            "       tr.modified " +
            " from transactions tr " +
            "   join accounts ac on tr.account_id = ac.id " +
            "   join categories ct on tr.category_id = ct.id ";

    @Query("select case when count(*) = 1 then 1 else 0 end " +
            "from transactions tr " +
            "where tr.transaction_date = cast(:transactionDate as timestamp) " +
            "and tr.amount = :amount")
    boolean existsTransactionByTransactionDateAndAmount(Date transactionDate, Double amount);

    List<Transaction> findAll();

    @Query(TRANSACTION_PROJECTIONS_QUERY +
            " where (:dateFrom is null or :dateFrom < tr.transaction_date) " +
            " and (:dateTo is null or :dateTo > tr.transaction_date) " +
            " and (:categoryId is null or ct.id = :categoryId)")
    List<TransactionProjection> getAllProjections(LocalDateTime dateFrom, LocalDateTime dateTo, Long categoryId);

    @Query(TRANSACTION_PROJECTIONS_QUERY +
            " where (:dateFrom is null or :dateFrom < tr.transaction_date) " +
            " and (:dateTo is null or :dateTo > tr.transaction_date) " +
            " and (:categoryId is null or ct.id = :categoryId)" +
            " and tr.amount < 0 and tr.status = 'OK'")
    List<TransactionProjection> getDischarges(LocalDateTime dateFrom, LocalDateTime dateTo, Long categoryId);

    @Query(TRANSACTION_PROJECTIONS_QUERY +
            " where (:dateFrom is null or :dateFrom < tr.transaction_date) " +
            " and (:dateTo is null or :dateTo > tr.transaction_date) " +
            " and (:categoryId is null or ct.id = :categoryId)" +
            " and tr.amount > 0 and tr.status = 'OK'")
    List<TransactionProjection> getGains(LocalDateTime dateFrom, LocalDateTime dateTo, Long categoryId);

    @Query("select tr.id, " +
            "       ac.account_name card_number, " +
            "       ct.category_name category, " +
            "       tr.status, " +
            "       tr.amount operation_amount, " +
            "       ac.currency, " +
            "       tr.cashback, " +
            "       tr.transaction_date, " +
            "       tr.description, " +
            "       tr.created, " +
            "       tr.modified " +
            " from transactions tr " +
            "   join accounts ac on tr.account_id = ac.id " +
            "   join categories ct on tr.category_id = ct.id " +
            " where (:dateFrom is null or :dateFrom < tr.transaction_date) " +
            " and (:dateTo is null or :dateTo > tr.transaction_date) " +
            " and ct.id = :categoryId " +
            " order by tr.id")
    List<TransactionProjection> getSalaries(LocalDateTime dateFrom, LocalDateTime dateTo, Long categoryId);

    @Modifying
    @Query("UPDATE transactions " +
            "SET category_id = :categoryId " +
            "WHERE id = :transactionId ")
    boolean updateTransactionCategoryById(Long categoryId, Long transactionId);

    @Query("SELECT tr.id " +
            "FROM transactions tr " +
            "JOIN categories ct ON ct.id = :categoryId " +
            "WHERE EXISTS ( " +
            "    SELECT 1 " +
            "    FROM unnest(ct.category_substrings::text[]) AS substring " +
            "    WHERE tr.description LIKE '%' || substring || '%' " +
            ")")
    List<Long> getTransactionIdsByCategorySubstrings(Long categoryId);

    @Modifying
    @Query("UPDATE transactions " +
            "SET category_id = :categoryId " +
            "WHERE id in ( " +
            "   SELECT tr.id " +
            "       FROM transactions tr " +
            "   WHERE EXISTS ( " +
            "       SELECT 1 " +
            "           FROM unnest(:categorySubstrings::text[]) AS substring " +
            "       WHERE tr.description LIKE '%' || substring || '%' " +
            ")) ")
    boolean updateTransactionCategoryByCategorySubstrings(List<String> categorySubstrings, Long categoryId);

    @Modifying
    @Query("UPDATE transactions " +
            "SET is_joined = :isJoined " +
            "WHERE id in (:transactionIds) ")
    boolean updateIsJoinedByTransactionId(Boolean isJoined, List<Long> transactionIds);

    @Modifying
    @Query("delete from transactions where id = :id")
    Long deleteByTransactionId(Long id);

}
