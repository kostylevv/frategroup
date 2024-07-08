package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Repository
@Primary
public class DataBaseUserStorage extends BaseStorage<User> implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO app_user(email, login, name, birthday) VALUES (?, ?, ?, ?)";

    public DataBaseUserStorage(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    public Collection<User> getAllUsers() {
        return List.of();
    }

    @Override
    public User createUser(User user) {
        Integer id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        return null;
    }

    @Override
    public User findUserById(Integer id) {
        return null;
    }
}
