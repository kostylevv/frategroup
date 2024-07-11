package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class DataBaseGenreStorage extends BaseStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";

    public DataBaseGenreStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return findManyNoExtractor(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);

    }
}
