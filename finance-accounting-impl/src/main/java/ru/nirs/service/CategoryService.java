package ru.nirs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nirs.dao.CategoryDao;
import ru.nirs.dao.TransactionDao;
import ru.nirs.entity.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Сервис для работы с категориями
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TransactionDao transactionDao;

    public Set<Category> getCategoriesByDateFromAndDateTo(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return Set.copyOf(categoryDao.getCategoriesByDateFromAndDateTo(dateFrom, dateTo));
    }

    public Set<Category> getAll() {
        return Set.copyOf(categoryDao.findAll());
    }

    public Category getCategoryById(Long id) {
        return categoryDao.getById(id);
    }

    @Transactional
    public Long createCategory(Category category, Boolean recalculateTransactions) {
        var now = LocalDateTime.now();
        var categoryId =  categoryDao.save(category.toBuilder()
                .createdDatetime(now)
                .modifiedDatetime(now)
                .build()
        ).getId();
        if (recalculateTransactions) {
            List<Long> transactionsToUpdate = transactionDao.getTransactionIdsByCategorySubstrings(categoryId);
            transactionsToUpdate.forEach(id -> transactionDao.updateTransactionCategoryById(categoryId, id));
        }
        return categoryId;
    }

    @Transactional
    public Long updateCategory(Category category, Boolean recalculateTransactions, Long categoryReplacementId) {
        var categoryToUpdate = categoryDao.getById(category.getId());
        if (!categoryToUpdate.getCategorySubstrings().equals(category.getCategorySubstrings())) {
            Set<String> oldSubstrings = new HashSet<>(categoryToUpdate.getCategorySubstrings());
            Set<String> newSubstrings = new HashSet<>(category.getCategorySubstrings());

            // Подстроки, которые были добавлены
            Set<String> addedSubstrings = new HashSet<>(newSubstrings);
            addedSubstrings.removeAll(oldSubstrings);

            // Подстроки, которые были удалены
            Set<String> removedSubstrings = new HashSet<>(oldSubstrings);
            removedSubstrings.removeAll(newSubstrings);

            // Обновляем категорию у транзакций для добавленных подстрок
            transactionDao.updateTransactionCategoryByCategorySubstrings(new ArrayList<>(addedSubstrings),
                    category.getId());

            // Обновляем категорию у транзакций для удаленных подстрок на categoryReplacementId
            transactionDao.updateTransactionCategoryByCategorySubstrings(new ArrayList<>(removedSubstrings),
                    categoryReplacementId);
        }

        var categoryId =  categoryDao.save(category.toBuilder()
                .modifiedDatetime(LocalDateTime.now())
                .build()
        ).getId();
        if (recalculateTransactions) {
            List<Long> transactionsToUpdate = transactionDao.getTransactionIdsByCategorySubstrings(categoryId);
            transactionsToUpdate.forEach(id -> transactionDao.updateTransactionCategoryById(categoryId, id));
        }
        return categoryId;
    }

}
