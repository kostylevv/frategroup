package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserResultSetExtractor implements ResultSetExtractor<User> {

    @Override
    public User extractData(ResultSet rs) throws SQLException {
        User user = null;
        Set<Integer> friends = new HashSet<>();

        while (rs.next()) {
            if (user == null) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setName(rs.getString("name"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                user.setFriends(friends);
            }

            int friendId = rs.getInt("friend_id");
            if (!rs.wasNull()) {
                friends.add(friendId);
            }
        }

        if (user != null && user.getFriends().contains(0)) {
            user.setFriends(Collections.emptySet());
        }

        return user;
    }
}
