package ru.nirs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для проекции сущности "Бюджет"
 */
@Getter
@Setter
@Validated
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Schema(description = "DTO для проекции сущности 'Бюджет'")
public final class BudgetProjectionDto {

    private final Long id;

    private final String userName;

    private final String categoryName;

    private final BigDecimal amount;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final LocalDateTime createdDatetime;

    private final LocalDateTime modifiedDatetime;

}
