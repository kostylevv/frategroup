package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Получен запрос на вывод всех пользователей");
        return userStorage.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Получен запрос на обновление пользователя");
        return userStorage.updateUser(updatedUser);
    }
}
