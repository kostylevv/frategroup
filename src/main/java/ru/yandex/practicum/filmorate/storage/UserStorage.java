package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User updatedUser);

    Optional<User> findUserById(Integer id);

    Optional<User> addFriend(Integer userId, Integer friendId);
}
