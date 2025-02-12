package ru.nirs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nirs.dao.BudgetDao;
import ru.nirs.dao.GoalDao;
import ru.nirs.entity.Budget;
import ru.nirs.entity.BudgetProjection;
import ru.nirs.entity.Goal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

/**
 * Сервис для работы с целями
 */
@Service
public class GoalService {

    private static final String ERROR_GOAL_NOT_FOUND = "Goal with id = %s doesn't exist";

    @Autowired
    private GoalDao goalDao;

    public List<Goal> getAll() {
        return goalDao.findAll();
    }

    public Goal getGoalById(Long id) {
        return goalDao.getById(id);
    }

    @Transactional
    public Long create(Goal goal) {
        var now = LocalDateTime.now();
        return goalDao.save(goal.toBuilder()
                .createdDatetime(now)
                .modifiedDatetime(now)
                .build()
        ).getId();
    }

    public void update(@Valid Goal goal) {
        goalDao.findById(goal.getId())
                .map(budgetSaved -> budgetSaved.toBuilder()
                        .modifiedDatetime(LocalDateTime.now())
                        .build())
                .map(goalDao::save)
                .orElseThrow(
                        () -> new IllegalArgumentException(format(ERROR_GOAL_NOT_FOUND, goal.getId())));
    }

    public Long delete(@NotNull Long id) {
        return goalDao.deleteByGoalId(id);
    }

}
