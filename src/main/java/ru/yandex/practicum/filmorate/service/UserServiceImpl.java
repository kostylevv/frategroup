package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
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
    public User updateUser(User updatedUser) {
        return userStorage.updateUser(updatedUser);
    }

    @Override
    public Collection<User> getAllFriends(Integer id) {
        User selectedUser = userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
        log.info("Выводится список друзей пользователя id={}", id);
        return selectedUser.getFriends().stream()
                .map(userStorage::findUserById)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        User friend = userStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        user.addUserIdToFriendsList(friendId);
        friend.addUserIdToFriendsList(userId);
        log.info("Пользователь id={} добавился в друзья к пользователю id={}", userId, friendId);
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        User friend = userStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

//      Здесь я хотел выбросить исключение, если пользователи не в друзьях, но тесты постман ожидают код 200))
//        if (!user.getFriends().contains(friendId)) {
//            log.warn("У пользователя с id={} нет друга с id={}",userId, friendId);
//            throw new NotFoundException("У пользователя с id=" + userId + " нет друга с id=" + friendId);
//        }
        user.deleteUserIdFromFriendsList(friendId);
        friend.deleteUserIdFromFriendsList(userId);
        log.info("Пользователь id={} удален из друзей пользователя id={}", userId, friendId);
        return user;
    }

    @Override
    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        User user1 = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
        User user2 = userStorage.findUserById(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + otherId));

        List<User> user1Friends = user1.getFriends().stream()
                .map(userStorage::findUserById)
                .flatMap(Optional::stream)
                .toList();
        List<User> user2Friends = user2.getFriends().stream()
                .map(userStorage::findUserById)
                .flatMap(Optional::stream)
                .toList();

        List<User> mutualFriends = new ArrayList<>(user1Friends);
        mutualFriends.retainAll(user2Friends);
        log.info("Выводится список общих друзей полхователей id={} и id={}", userId, otherId);
        return mutualFriends;
    }

}
