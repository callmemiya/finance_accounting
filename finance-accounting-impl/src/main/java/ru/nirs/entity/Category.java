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
import java.util.List;

/**
 * Сущность "Категория"
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("categories")
public final class Category {

    @Id
    private final Long id;

    private final String categoryName;

    private final List<String> categorySubstrings;

    /** Технические дата и время создания (UTC) */
    @Column("created")
    private final LocalDateTime createdDatetime;

    /** Технические дата и время изменения (UTC) */
    @Column("modified")
    private final LocalDateTime modifiedDatetime;

}
