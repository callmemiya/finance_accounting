package ru.nirs.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.nirs.entity.Budget;
import ru.nirs.entity.BudgetProjection;
import ru.nirs.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с {@link Budget}
 */
public interface BudgetDao extends CrudRepository<Budget, Long> {

    List<Budget> findAll();

    Budget getById(Long id);

    @Query("SELECT CASE WHEN COUNT(*) >= 1 THEN 1 ELSE 0 END " +
            "FROM budgets bg " +
            "WHERE bg.budget_name = :name ")
    boolean checkIfExistByName(String name);

    @Query("select * " +
            "from budgets bg " +
            "where bg.budget_name = :name")
    Optional<Budget> getByBudgetName(String name);

    @Query("select * " +
            "from budgets bg " +
            "join users u on bg.user_id = u.id " +
            "join categories ct on bg.category_id = ct.id")
    List<BudgetProjection> getProjectionList();

}
