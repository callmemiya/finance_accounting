package ru.nirs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для сущности "Бюджет"
 */
@Getter
@Setter
@Validated
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Schema(description = "DTO для сущности 'Бюджет'")
public final class BudgetDto {

    private final Long id;

    private final Long userId;

    private final Long categoryId;

    private final BigDecimal amount;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final LocalDateTime createdDatetime;

    private final LocalDateTime modifiedDatetime;

}
