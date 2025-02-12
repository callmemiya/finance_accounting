package ru.nirs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nirs.dto.BudgetDto;
import ru.nirs.dto.BudgetProjectionDto;
import ru.nirs.dto.GoalDto;
import ru.nirs.entity.Budget;
import ru.nirs.entity.BudgetProjection;
import ru.nirs.entity.Goal;

import java.util.List;

/**
 * Маппер для {@link GoalDto}
 */
@Mapper
public interface GoalMapper {

    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    GoalDto toDto(Goal goal);

    Goal toEntity(GoalDto goalDto);

    List<GoalDto> toDtoList(List<Goal> list);

}
