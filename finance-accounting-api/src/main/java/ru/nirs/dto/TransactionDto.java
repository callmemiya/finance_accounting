package ru.nirs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO для сущности "Транзакция"
 */
@Getter
@Setter
@Validated
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Schema(description = "DTO для сущности 'Транзакция'")
public final class TransactionDto {

    private final Long id;

    private final Long accountId;

    private final Long categoryId;

    private final String status;

    private final Double amount;

    private final Double cashback;

    private final Date transactionDate;

    private final String description;

    private final LocalDateTime createdDatetime;

    private final LocalDateTime modifiedDatetime;

}
