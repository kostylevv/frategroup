package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Получен запрос на вывод всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Получен запрос на обновление пользователя");
        return userService.updateUser(updatedUser);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUsersFriends(@PathVariable Integer id) {
        log.info("Получен запрос на вывод друзей пользователя с id={}", id);
        return userService.getAllFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable("id") Integer userId,
                             @PathVariable Integer friendId) {
        log.info("Получен запрос на добавление в друзья ползователю id={} от пользователя id={}", userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriends(@PathVariable("id") Integer userId,
                                  @PathVariable Integer friendId) {
        log.info("Получен запрос на удаление из друзей пользователя id={} пользователя id={}", userId, friendId);
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable("id") Integer userId,
                                             @PathVariable Integer otherId) {
        log.info("Получен запрос вывод общиех друзей пользователей id={} и id={}", userId, otherId);
        return userService.getMutualFriends(userId, otherId);
    }
}
