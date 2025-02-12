package ru.nirs.dao;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.nirs.entity.BudgetProjection;
import ru.nirs.entity.Goal;

import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с {@link Goal}
 */
public interface GoalDao extends CrudRepository<Goal, Long> {

    List<Goal> findAll();

    Goal getById(Long id);

    @Modifying
    @Query("delete from goals where id = :id")
    Long deleteByGoalId(Long id);

}
