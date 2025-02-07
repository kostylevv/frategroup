package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validators.UserValidator.isUserInfoValid;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto findUserById(Integer id) {
        return userStorage.findUserById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с ID: " + id));
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        User user = UserMapper.mapToUser(userRequest);
        try {
            isUserInfoValid(user);
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
        } catch (ValidationException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
        user = userStorage.createUser(user);
        log.info("Пользователь создан: {}", user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request) {
        if (request.getId() == null) {
            log.warn("Не указан id пользователя для обновления");
            throw new ValidationException("Id должен быть указан.");
        }

        Optional<User> userOpt = userStorage.findUserById(request.getId());
        if (userOpt.isEmpty()) {
            log.warn("Пользователь с id {} не найден", request.getId());
            throw new NotFoundException("Пользователь с указанным id = " + request.getId() + " не найден.");
        }

        User userToUpdate = userOpt.get();
        User updatedUser = UserMapper.updateUserFields(userToUpdate, request);

        try {
            isUserInfoValid(updatedUser);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
            throw exception;
        }
        updatedUser = userStorage.updateUser(updatedUser);
        log.info("Обновленный пользователь {} сохранен", updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }


    @Override
    public Collection<UserDto> getAllFriends(Integer id) {
        User selectedUser = userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
        log.info("Выводится список друзей пользователя id={}", id);
        return userStorage.getAllFriends(selectedUser.getId()).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto addFriend(Integer userId, Integer friendId) {

        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
        User friend = userStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        userStorage.addFriend(user.getId(), friend.getId());

        user.addUserIdToFriendsList(friendId);
        log.info("Пользователь id={} добавился в друзья к пользователю id={}", userId, friendId);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        User friend = userStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

//      Здесь я хотел выбросить исключение, если пользователи не в друзьях, но тесты постман ожидают код 200))
        userStorage.deleteFriend(userId, friendId);
        user.deleteUserIdFromFriendsList(friendId);
        friend.deleteUserIdFromFriendsList(userId);
        log.info("Пользователь id={} удален из друзей пользователя id={}", userId, friendId);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> getMutualFriends(Integer userId, Integer otherId) {
        User user1 = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
        User user2 = userStorage.findUserById(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + otherId));

        Collection<UserDto> user1Friends = getAllFriends(user1.getId());
        Collection<UserDto> user2Friends = getAllFriends(user2.getId());

        List<UserDto> mutualFriends = new ArrayList<>(user1Friends);
        mutualFriends.retainAll(user2Friends);
        log.info("Выводится список общих друзей полхователей id={} и id={}", userId, otherId);
        return mutualFriends;
    }

}
