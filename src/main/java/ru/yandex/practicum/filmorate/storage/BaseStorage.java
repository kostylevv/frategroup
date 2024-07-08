package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.CreateUserException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public class BaseStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;


    protected Integer insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);


        Integer id = keyHolder.getKeyAs(Integer.class);

        if (id != null) {
            return id;
        } else {
            throw new CreateUserException("Не удалось сохранить пользователя");
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

}