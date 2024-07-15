package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ExtractException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserListResultSetExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException {
        Map<Integer, User> userIdToUser = new HashMap<>();

        while (rs.next()) {
            int userId = rs.getInt("id");
            User user = userIdToUser.computeIfAbsent(userId, id -> {
                User newUser = new User();
                newUser.setId(id);
                try {
                    newUser.setEmail(rs.getString("email"));
                    newUser.setLogin(rs.getString("login"));
                    newUser.setName(rs.getString("name"));
                    newUser.setBirthday(rs.getDate("birthday").toLocalDate());
                } catch (SQLException e) {
                    throw new ExtractException("Не удалось считать данные из базы данных");
                }
                newUser.setFriends(new HashSet<>());
                return newUser;
            });

            int friendId = rs.getInt("friend_id");
            if (!rs.wasNull()) {
                user.getFriends().add(friendId);
            }
        }

        List<User> users = new ArrayList<>(userIdToUser.values());
        for (User user : users) {
            if (user.getFriends().contains(0)) {
                user.setFriends(Collections.emptySet());
            }
        }

        return users;
    }
}
