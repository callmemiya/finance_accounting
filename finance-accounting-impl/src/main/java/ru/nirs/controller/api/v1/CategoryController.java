package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nirs.mapper.CategoryMapper;
import ru.nirs.dto.CategoryDto;
import ru.nirs.service.CategoryService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Контроллер для работы с категориями
 */
@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    private static final String ERROR_CATEGORY_UPDATE = "Category update failed! Path id = %s and dto id = %s are not equals or null";

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    @Operation(summary = "Получение данных о категориях согласно параметрам")
    public Set<CategoryDto> getCategoriesByDateFromAndDateTo(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return CategoryMapper
                .INSTANCE
                .toDtoSet(categoryService.getCategoriesByDateFromAndDateTo(dateFrom, dateTo));
    }

    @GetMapping("/all")
    @Operation(summary = "Получение данных о категориях")
    public Set<CategoryDto> getAll() {
        return CategoryMapper
                .INSTANCE
                .toDtoSet(categoryService.getAll());
    }

    @PostMapping()
    @Operation(summary = "Создание категории")
    public Long createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(CategoryMapper.INSTANCE.toEntity(categoryDto), categoryDto.getRecalculateTransactions());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление категории")
    public Long updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        if (!Objects.equals(categoryDto.getId(), id) || categoryDto.getId() == null) {
            throw new IllegalArgumentException(ERROR_CATEGORY_UPDATE);
        }
        return categoryService.updateCategory(CategoryMapper.INSTANCE.toEntity(categoryDto),
                categoryDto.getRecalculateTransactions(),
                categoryDto.getCategoryReplacementId());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Удаление категории по уникальному идентификатору")
    public CategoryDto getById(@PathVariable @Parameter(description = "Идентификатор категории") Long id) {
        return CategoryMapper
                .INSTANCE
                .toDto(categoryService.getCategoryById(id));
    }

}
