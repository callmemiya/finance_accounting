package ru.nirs.controller.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nirs.dto.AuthResponseDto;
import ru.nirs.dto.LoginRequestDto;
import ru.nirs.dto.RegistrationRequestDto;
import ru.nirs.entity.User;
import ru.nirs.security.JwtUtils;
import ru.nirs.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Аутентификация", description = "API для регистрации и входа в систему")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegistrationRequestDto request) {
        // Создаем нового пользователя
        User user = userService.createUser(
            request.getUsername(),
            request.getEmail(),
            request.getPassword()
        );

        // Генерируем JWT токен
        String token = jwtUtils.generateToken(user);

        // Возвращаем токен и ID пользователя
        return ResponseEntity.ok(AuthResponseDto.builder()
                .token(token)
                .userId(user.getId())
                .build());
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        // Проверяем логин и пароль
        User user = userService.login(request.getUsername(), request.getPassword());

        // Генерируем JWT токен
        String token = jwtUtils.generateToken(user);
        System.out.println(token);

        // Возвращаем токен и ID пользователя
        return ResponseEntity.ok(AuthResponseDto.builder()
                .token(token)
                .userId(user.getId())
                .build());
    }
} 