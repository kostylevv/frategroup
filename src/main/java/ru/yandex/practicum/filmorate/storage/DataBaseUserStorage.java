package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class DataBaseUserStorage extends BaseStorage<User> implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO app_user(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM app_user";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM app_user WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE app_user SET email = ?, login = ?, name = ?, birthday = ?" +
            " WHERE id = ?";

    public DataBaseUserStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
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
    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}
