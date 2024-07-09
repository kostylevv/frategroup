package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserService {

    UserDto findUserById(Integer id);

    Collection<UserDto> getAllUsers();

    UserDto createUser(NewUserRequest user);

    UserDto updateUser(UpdateUserRequest updatedUser);

    Collection<UserDto> getAllFriends(Integer id);

    UserDto addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer userId, Integer friendId);

    Collection<User> getMutualFriends(Integer userId, Integer otherId);
}
