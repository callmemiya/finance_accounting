package ru.nirs.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.nirs.entity.Account;

import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с {@link Account}
 */
public interface AccountDao extends CrudRepository<Account, Long> {

    List<Account> findAll();

    @Query("SELECT CASE WHEN COUNT(*) >= 1 THEN 1 ELSE 0 END " +
            "FROM accounts ac " +
            "WHERE ac.account_name = :name ")
    boolean checkIfExistByName(String name);

    @Query("select * " +
            "from accounts ac " +
            "where ac.account_name = :name")
    Optional<Account> getByAccountName(String name);

}
