package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static ru.yandex.practicum.filmorate.validators.UserValidator.isUserInfoValid;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Получен запрос на вывод всех пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        try {
            if (isUserInfoValid(user) && !isEmailDuplicated(user.getEmail())) {
                user.setId(getNextUserId());

                if (user.getName() == null) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.info("Пользователь создан: {}", user);
            }
        } catch (ValidationException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Получен запрос на обновление пользователя");
        //пустое поле при парсинге для примитива устанавливается 0,
        // проверяем это вместо null (id в программе начинаются с 1)
         if (updatedUser.getId() == 0) {
             log.warn("Не указан id пользователя для обновления");
            throw new ValidationException("Id должен быть указан.");
        }

        User oldUser = users.get(updatedUser.getId());
        if (oldUser == null) {
            log.warn("Пользователь с id {} не найден", updatedUser.getId());
            throw new NotFoundException("Пользователь с указанным id = " + updatedUser.getId() + " не найден.");
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(oldUser.getEmail())) {
            if (isEmailDuplicated(updatedUser.getEmail())) {
                log.warn("Email {} уже используется", updatedUser.getEmail());
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            oldUser.setEmail(updatedUser.getEmail());
            log.info("Email пользователя обновлен на {}", oldUser.getEmail());
        }

        if (updatedUser.getLogin() != null && !updatedUser.getLogin().isEmpty()) {
            oldUser.setLogin(updatedUser.getLogin());
            log.info("Логин пользователя обновлен на {}", oldUser.getLogin());
        }

        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
            oldUser.setName(updatedUser.getName());
            log.info("Имя пользователя обновлено на {}", oldUser.getName());
        }

        if (updatedUser.getBirthday() != null &&
                updatedUser.getBirthday().isBefore(LocalDate.now())) {
            oldUser.setBirthday(updatedUser.getBirthday());
            log.info("Дата рождения пользователя обновлена на {}", oldUser.getBirthday());
        }

        users.put(oldUser.getId(), oldUser);
        log.info("Обновленный пользователь {} сохранен", oldUser);
        return oldUser;
    }

     private boolean isEmailDuplicated(String newEmail) {
        return users.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(newEmail));
    }

    private long getNextUserId() {
        if (users.isEmpty()) {
            return 1;
        }
        Long currentMaxId = Collections.max(users.keySet());
        return ++currentMaxId;
    }

}
