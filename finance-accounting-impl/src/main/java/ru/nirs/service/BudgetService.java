package ru.nirs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nirs.dao.BudgetDao;
import ru.nirs.entity.Budget;
import ru.nirs.entity.BudgetProjection;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

/**
 * Сервис для работы с бюджетами
 */
@Service
public class BudgetService {

    private static final String ERROR_BUDGET_NOT_FOUND = "Budget with id = %s doesn't exist";

    @Autowired
    private BudgetDao budgetDao;

    public List<BudgetProjection> getAll() {
        return budgetDao.getProjectionList();
    }

    public Budget getBudgetById(Long id) {
        return budgetDao.getById(id);
    }

    @Transactional
    public Long create(Budget budget) {
        var now = LocalDateTime.now();
        var budgetId =  budgetDao.save(budget.toBuilder()
                .createdDatetime(now)
                .modifiedDatetime(now)
                .build()
        ).getId();
        return budgetId;
    }

    public void update(@Valid Budget budget) {
        budgetDao.findById(budget.getId())
                .map(budgetSaved -> budgetSaved.toBuilder()
                        .modifiedDatetime(LocalDateTime.now())
                        .build())
                .map(budgetDao::save)
                .orElseThrow(
                        () -> new IllegalArgumentException(format(ERROR_BUDGET_NOT_FOUND, budget.getId())));
    }

}
