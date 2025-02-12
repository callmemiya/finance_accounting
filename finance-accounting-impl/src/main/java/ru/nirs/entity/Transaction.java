package ru.nirs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Сущность "Транзакция"
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("transactions")
public final class Transaction {

    @Id
    private final Long id;

    @NotNull
    private final Long accountId;

    @NotNull
    private final Long categoryId;

    @NotBlank
    private final String status;

    @NotNull
    private final Double amount;

    private final Double cashback;

    @NotNull
    private final Date transactionDate;

    private final String description;

    /** Технические дата и время создания (UTC) */
    @Column("created")
    private final LocalDateTime createdDatetime;

    /** Технические дата и время изменения (UTC) */
    @Column("modified")
    private final LocalDateTime modifiedDatetime;

}
