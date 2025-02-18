package ru.nirs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nirs.dao.UserDao;
import ru.nirs.entity.User;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String username, String email, String password) {
        // Проверяем, что логин и email уникальны
        if (userDao.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        if (userDao.existsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        // Создаем нового пользователя с захешированным паролем
        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .build();

        return userDao.save(user);
    }

    public User findByLogin(String login) {
        return userDao.findByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    public boolean validatePassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPasswordHash());
    }
} 