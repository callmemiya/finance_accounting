package ru.nirs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для сущности "Цель"
 */
@Getter
@Setter
@Validated
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Schema(description = "DTO для сущности 'Цель'")
public final class GoalDto {

    private final Long id;

    private final Long userId;

    private final String goalName;

    private final BigDecimal targetAmount;

    private final BigDecimal currentAmount;

    private final LocalDate deadline;

    private final LocalDateTime createdDatetime;

    private final LocalDateTime modifiedDatetime;

}
