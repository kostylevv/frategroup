package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmService {

    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Collection<Film> getPopularFilms(int count);

    Film addLike(Integer filmId, Integer userId);

    Film deleteLike(Integer filmId, Integer userId);
}
