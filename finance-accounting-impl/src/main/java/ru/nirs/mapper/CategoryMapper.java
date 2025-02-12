package ru.nirs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nirs.entity.Category;
import ru.nirs.dto.CategoryDto;

import java.util.List;
import java.util.Set;

/**
 * Маппер для {@link CategoryDto}
 */
@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    List<CategoryDto> toDtoList(List<Category> list);

    Set<CategoryDto> toDtoSet(Set<Category> set);

}
