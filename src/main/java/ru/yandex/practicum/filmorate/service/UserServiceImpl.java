package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserStorage userStorage;

    @Override
    public Collection<User> getAllFriends(Integer id) {
        User selectedUser = userStorage.findUserById(id);
        log.info("Выводится список друзей пользователя id={}", id);
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
        log.info("Пользователь id={} добавился в друзья к пользователю id={}", userId, friendId);
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
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
        log.info("Выводится список общих друзей полхователей id={} и id={}", userId, otherId);
        return mutualFriends;
    }

}
