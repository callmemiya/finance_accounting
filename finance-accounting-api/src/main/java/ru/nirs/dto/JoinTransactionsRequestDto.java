package ru.nirs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO для запроса на объединение транзакций
 */
@Getter
@Setter
@Validated
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Schema(description = "DTO для запроса на объединение транзакций")
public final class JoinTransactionsRequestDto {

    @NotEmpty(message = "Список ID транзакций не может быть пустым")
    @Schema(description = "Список ID транзакций для объединения")
    private final List<Long> transactionIds;

    @NotNull(message = "ID категории не может быть пустым")
    @Schema(description = "ID категории для объединенной транзакции")
    private final Long categoryId;

    @NotNull(message = "Описание не может быть пустым")
    @Schema(description = "Описание для объединенной транзакции")
    private final String description;
} 