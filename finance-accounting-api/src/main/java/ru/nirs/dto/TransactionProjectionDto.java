package ru.nirs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для сущности "Проекция транзакции"
 */
@Getter
@Setter
@Validated
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Schema(description = "DTO для сущности 'Проекция транзакции'")
public final class TransactionProjectionDto {

    @Schema(description = "Уникальный идентификатор")
    private final Long id;

    @Schema(description = "Дата операции")
    private final LocalDate transactionDate;

    @Schema(description = "Номер карты")
    private final String cardNumber;

    @Schema(description = "Статус операции")
    private final String status;

    @Schema(description = "Сумма операции")
    private final Double operationAmount;

    @Schema(description = "Валюта")
    private final String currency;

    @Schema(description = "Кэшбек оепрации")
    private final Double cashback;

    @Schema(description = "Категория")
    private final String category;

    @Schema(description = "Описание")
    private final String description;

    @Schema(description = "Технические дата и время создания")
    private final LocalDateTime createdDatetime;

    @Schema(description = "Технические дата и время изменения")
    private final LocalDateTime modifiedDatetime;

}
