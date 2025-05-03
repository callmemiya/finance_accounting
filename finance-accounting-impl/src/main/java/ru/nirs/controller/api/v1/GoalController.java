package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nirs.dto.GoalDto;
import ru.nirs.mapper.GoalMapper;
import ru.nirs.service.GoalService;

import java.util.List;

import static java.lang.String.format;

/**
 * Контроллер для работы с целями
 */
@RestController
@RequestMapping(value = "/goals")
public class GoalController {

    private static final String ERROR_GOAL_DELETE = "Unable to delete Goal with id = %s";

    @Autowired
    private GoalService goalService;

    @GetMapping()
    public List<GoalDto> getAll() {
        return GoalMapper
                .INSTANCE
                .toDtoList(goalService.getAll());
    }

    @PostMapping()
    @Operation(summary = "Создание цели")
    public Long create(@RequestBody GoalDto goalDto) {
        return goalService.create(GoalMapper.INSTANCE.toEntity(goalDto));
    }

    @PutMapping()
    @Operation(summary = "Обновление цели")
    public void update(@RequestBody GoalDto goalDto) {
        goalService.update(GoalMapper.INSTANCE.toEntity(goalDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение данных о цели по уникальному идентификатору")
    public GoalDto getById(@PathVariable @Parameter(description = "Идентификатор цели") Long id) {
        return GoalMapper
                .INSTANCE
                .toDto(goalService.getGoalById(id));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление цели по уникальному идентификатору")
    public Long deleteBy(@PathVariable @Parameter(description = "Идентификатор цели", required = true) Long id) {
        Long deletedCount = goalService.delete(id);
        if (deletedCount > 0) {
            return id;
        }
        throw new IllegalArgumentException(format(ERROR_GOAL_DELETE, id));
    }

}
