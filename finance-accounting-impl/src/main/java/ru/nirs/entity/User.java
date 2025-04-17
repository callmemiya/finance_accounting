package ru.nirs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @Id
    private Long id;

    /** Имя пользователя */
    private String username;

    /** Хеш пароля */
    private String passwordHash;

    /** Электронная почта */
    private String email;

    /** Технические дата и время создания (UTC) */
    private LocalDateTime created;

    /** Технические дата и время изменения (UTC) */
    private LocalDateTime modified;

} 