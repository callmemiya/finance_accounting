package ru.nirs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

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
public final class TransactionProjection {

    @Id
    private final Long id;

    private final Date transactionDate;

    private final String cardNumber;

    private final String status;

    private final Double operationAmount;

    private final String currency;

    private final Double cashback;

    private final String category;

    private final String description;

    /** Технические дата и время создания (UTC) */
    @Column("created")
    private final LocalDateTime createdDatetime;

    /** Технические дата и время изменения (UTC) */
    @Column("modified")
    private final LocalDateTime modifiedDatetime;

}
