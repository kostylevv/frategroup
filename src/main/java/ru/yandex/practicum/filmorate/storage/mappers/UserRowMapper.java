package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());

        Set<Integer> friendIds = new HashSet<>();
        do {
            int friendId = resultSet.getInt("friend_id");
            if (friendId > 0 && !resultSet.wasNull()) {
                friendIds.add(friendId);
            }
        } while (resultSet.next() && resultSet.getInt("id") == user.getId());

        user.setFriends(friendIds);

        return user;
    }
}

