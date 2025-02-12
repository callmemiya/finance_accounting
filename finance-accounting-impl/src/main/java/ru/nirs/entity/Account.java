package ru.nirs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Сущность "Категория"
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("accounts")
public final class Account {

    @Id
    private final Long id;

    private final Long userId;

    private final String accountName;

    private final Double balance;

    private final String currency;

    /** Технические дата и время создания (UTC) */
    @Column("created")
    private final LocalDateTime createdDatetime;

    /** Технические дата и время изменения (UTC) */
    @Column("modified")
    private final LocalDateTime modifiedDatetime;

}
