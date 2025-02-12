package ru.nirs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Сущность "Цель"
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("goals")
public final class Goal {

    @Id
    private final Long id;

    private final Long userId;

    private final String goalName;

    private final BigDecimal targetAmount;

    private final BigDecimal currentAmount;

    private final LocalDate deadline;

    /** Технические дата и время создания (UTC) */
    @Column("created")
    private final LocalDateTime createdDatetime;

    /** Технические дата и время изменения (UTC) */
    @Column("modified")
    private final LocalDateTime modifiedDatetime;

}
