package ru.nirs.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.nirs.entity.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с {@link Category}
 */
public interface CategoryDao extends CrudRepository<Category, Long> {

    List<Category> findAll();

    Category getById(Long id);

    @Query("SELECT CASE WHEN COUNT(*) >= 1 THEN 1 ELSE 0 END " +
            "FROM categories ct " +
            "WHERE ct.category_name = :name ")
    boolean checkIfExistByName(String name);

    @Query("select * " +
            "from categories ct " +
            "where ct.category_name = :name")
    Optional<Category> getByCategoryName(String name);

    @Query("SELECT * " +
            "FROM categories " +
            "WHERE EXISTS ( " +
            "    SELECT 1 " +
            "    FROM unnest(string_to_array(category_substrings, ',')) AS substring " +
            "    WHERE :description ILIKE '%' || substring || '%' " +
            ")")
    Optional<Category> getCategoryByDescription(String description);

    @Query("select ct.id, " +
            "      ct.category_name, " +
            "      ct.created, " +
            "      ct.modified " +
            " from transactions tr " +
            "   join categories ct on tr.category_id = ct.id " +
            " where (:dateFrom is null or :dateFrom < tr.transaction_date) " +
            " and (:dateTo is null or :dateTo > tr.transaction_date) " +
            " order by ct.category_name")
    List<Category> getCategoriesByDateFromAndDateTo(LocalDateTime dateFrom, LocalDateTime dateTo);

}
