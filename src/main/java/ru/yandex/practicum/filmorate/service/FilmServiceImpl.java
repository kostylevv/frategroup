package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public Collection<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .toList();
        return films.reversed();
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        film.addUserIdToFilmLikes(userId);
        return film;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        film.deleteUserIdFromFilmLikes(userId);
        return film;
    }
}
