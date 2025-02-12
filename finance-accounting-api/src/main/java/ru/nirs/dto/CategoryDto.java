package ru.nirs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для сущности "Категория"
 */
@Getter
@Setter
@Validated
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Schema(description = "DTO для сущности 'Категория'")
public final class CategoryDto {

    private Long id;

    private String categoryName;

    private List<String> categorySubstrings;

    private LocalDateTime createdDatetime;

    private LocalDateTime modifiedDatetime;

    private Boolean recalculateTransactions;

    private Long categoryReplacementId;

}
