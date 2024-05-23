package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            if (isUserInfoValid(user)) {
                user.setId(getNextUserId());
                if (user.getName().isEmpty() || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
            }
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        if (updatedUser.getId() == 0) {
            throw new ValidationException("Id должен быть указан.");
        }

        User oldUser = users.get(updatedUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с указанным id = " + updatedUser.getId() + " не найден.");
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(oldUser.getEmail())) {
            if (isEmailDuplicated(updatedUser.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            oldUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getLogin() != null && !updatedUser.getLogin().isEmpty()) {
            oldUser.setLogin(updatedUser.getLogin());
        }

        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
            oldUser.setName(updatedUser.getName());
        }

        if (updatedUser.getBirthday() != null && updatedUser.getBirthday().isBefore(Instant.now())) {
            oldUser.setBirthday(updatedUser.getBirthday());
        }

        users.put(oldUser.getId(), oldUser);
        return oldUser;
    }

    private boolean isUserInfoValid(User user) throws ValidationException {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный адрес электронной почты.");

        } else if (isEmailDuplicated(user.getEmail())) {
            throw new ValidationException("Этот имейл уже используется");

        } else if (user.getLogin().isBlank() || user.getLogin().isEmpty() || !user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");

        } else if (user.getBirthday().isAfter(Instant.now())) {
            throw new ValidationException("Некорректная дата рождения.");
        }
        return true;
    }

    private boolean isEmailDuplicated(String newEmail) {
        return users.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(newEmail));
    }

    private int getNextUserId() {
        if (users.isEmpty()) {
            return 1;
        }
        int currentMaxId = Collections.max(users.keySet());
        return ++currentMaxId;
    }

}
