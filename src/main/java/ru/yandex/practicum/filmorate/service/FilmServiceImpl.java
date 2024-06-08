package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .toList();
        log.info("Выводится список популярных фильмов");
        return films.reversed();
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        film.addUserIdToFilmLikes(user.getId());
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
        return film;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);

        if (!film.getLikes().contains(userId)) {
            log.warn("Лайк пользователя с id={} не найден у фильма id={}", userId, filmId);
            throw new NotFoundException("Лайк пользователя с id=" + userId + " не найден у фильма id=" + filmId);
        }

        film.deleteUserIdFromFilmLikes(user.getId());
        log.info("Пользователь id={} убрал лайк с фильма id={}", userId, filmId);
        return film;
    }
}
