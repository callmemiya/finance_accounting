package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nirs.dto.BudgetDto;
import ru.nirs.dto.BudgetProjectionDto;
import ru.nirs.mapper.BudgetMapper;
import ru.nirs.service.BudgetService;

import java.util.List;

/**
 * Контроллер для работы с бюджетами
 */
@RestController
@RequestMapping(value = "/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping()
    public List<BudgetProjectionDto> getAll() {
        return BudgetMapper
                .INSTANCE
                .toProjectionDtoList(budgetService.getAll());
    }

    @PostMapping()
    public Long createBudget(@RequestBody BudgetDto budgetDto) {
        return budgetService.create(BudgetMapper.INSTANCE.toEntity(budgetDto));
    }

    @PutMapping()
    public void updateBudget(@RequestBody BudgetDto budgetDto) {
        budgetService.update(BudgetMapper.INSTANCE.toEntity(budgetDto));
    }

    @GetMapping("/{id}")
    public BudgetDto getById(@PathVariable @Parameter(description = "Идентификатор бюджета") Long id) {
        return BudgetMapper
                .INSTANCE
                .toDto(budgetService.getBudgetById(id));
    }

}
