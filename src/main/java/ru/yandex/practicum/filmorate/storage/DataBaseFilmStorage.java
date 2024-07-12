package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Repository
@Primary
public class DataBaseFilmStorage extends BaseStorage<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY = "INSERT INTO film(name, description, release_date, duration, " +
            "mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";

    public DataBaseFilmStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return List.of();
    }

    @Override
    public Film createFilm(Film film) {


        Integer id = insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);


        //insert(INSERT_FILM_GENRES_QUERY, id, film.getGenres());


        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        return null;
    }

    @Override
    public Film findFilmById(Integer id) {
        return null;
    }
}
