package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class DataBaseFilmStorage extends BaseStorage<Film> implements FilmStorage {

    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, " +
            "mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.*, " +
                    "m.name AS mpa_name, " +
                    "fg.genre_id AS genres_id, " +
                    "g.name AS genre_name, " +
                    "fl.user_id AS likes_id " +
                    "FROM film f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "WHERE f.id = ?;";
    private static final String FIND_ALL_QUERY =
            "SELECT  f.*, " +
                    "m.name AS mpa_name, " +
                    "fg.genre_id AS genres_id, " +
                    "g.name AS genre_name, " +
                    "fl.user_id AS likes_id " +
                    "FROM film f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, " +
            "duration = ? WHERE id = ?";

    public DataBaseFilmStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return findManyNoExtractor(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> findFilmById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Film createFilm(Film film) {
        Integer id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);

        for (Genre genre : film.getGenres()) {
            insert(INSERT_FILM_GENRES_QUERY, id, genre.getId());
        }

        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        update(
                UPDATE_QUERY,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getId()
        );
        return updatedFilm;
    }


}
