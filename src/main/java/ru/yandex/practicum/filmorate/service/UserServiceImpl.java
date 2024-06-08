package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<User> getAllFriends(Integer id) {
        User selectedUser = userStorage.findUserById(id);
        return selectedUser.getFriends().stream()
                .map(userStorage::findUserById)
                .toList();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.addUserIdToFriendsList(friendId);
        friend.addUserIdToFriendsList(userId);
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.deleteUserIdFromFriendsList(friendId);
        friend.deleteUserIdFromFriendsList(userId);
        return user;
    }

    @Override
    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        User user1 = userStorage.findUserById(userId);
        User user2 = userStorage.findUserById(otherId);

        List<User> user1Friends = user1.getFriends().stream()
                .map(userStorage::findUserById)
                .toList();
        List<User> user2Friends = user2.getFriends().stream()
                .map(userStorage::findUserById)
                .toList();

        List<User> mutualFriends = new ArrayList<>(user1Friends);
        mutualFriends.retainAll(user2Friends);

        return mutualFriends;
    }

}
