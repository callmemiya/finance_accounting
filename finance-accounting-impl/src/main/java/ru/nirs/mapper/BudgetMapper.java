package ru.nirs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nirs.dto.BudgetDto;
import ru.nirs.dto.BudgetProjectionDto;
import ru.nirs.dto.CategoryDto;
import ru.nirs.entity.Budget;
import ru.nirs.entity.BudgetProjection;
import ru.nirs.entity.Category;

import java.util.List;
import java.util.Set;

/**
 * Маппер для {@link BudgetDto}
 */
@Mapper
public interface BudgetMapper {

    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    BudgetDto toDto(Budget budget);

    Budget toEntity(BudgetDto categoryDto);

    List<BudgetDto> toDtoList(List<Budget> list);

    BudgetProjectionDto toProjectionDto(BudgetProjection budget);

    List<BudgetProjectionDto> toProjectionDtoList(List<BudgetProjection> list);

}
