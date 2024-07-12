package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmService {

    FilmDto findFilmById(Integer id);

    Collection<Film> getAllFilms();

    FilmDto createFilm(NewFilmRequest request);

    Film updateFilm(Film updatedFilm);

    Collection<Film> getPopularFilms(int count);

    Film addLike(Integer filmId, Integer userId);

    Film deleteLike(Integer filmId, Integer userId);
}
