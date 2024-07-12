package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.isFilmInfoValid;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public FilmDto findFilmById(Integer id) {
        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto createFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapTofilm(request);

        try {
            isFilmInfoValid(film);
        } catch (ValidationException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        film = filmStorage.createFilm(film);
        FilmDto filmDto = FilmMapper.mapToFilmDto(film);

        Set<Genre> genres = film.getGenres().stream()
                .map(Genre::getId)
                .map(genreStorage::findGenreById)
                .flatMap(Optional::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Genre::getId))));
        filmDto.setGenres(genres);


        Integer mpaId = film.getMpa().getId();
        Mpa mpa = mpaStorage.findMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("Не найден рейтинг с ID:" + mpaId));
        filmDto.setMpa(mpa);

        log.info("Фильм создан: {}", filmDto);
        return filmDto;
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest request) {
        if (request.getId() == null) {
            log.warn("Не указан id фильма для обновления");
            throw new ValidationException("Id фильма для обновления должен быть указан.");
        }

        Optional<Film> filmOpt = filmStorage.findFilmById(request.getId());
        if (filmOpt.isEmpty()) {
            log.warn("Фильм с id {} не найден", request.getId());
            throw new NotFoundException("Фильм с указанным id = " + request.getId() + " не найден.");
        }

        Film filmToUpdate = filmOpt.get();
        Film updatedFilm = FilmMapper.updateFilmFields(filmToUpdate, request);

        try {
            isFilmInfoValid(updatedFilm);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
            throw exception;
        }
        updatedFilm = filmStorage.updateFilm(updatedFilm);
        log.info("Обновленный фильм {} сохранен", updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

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
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с ID: " + userId));
        film.addUserIdToFilmLikes(user.getId());
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
        return film;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Не найден фильм с ID:"));
        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с ID: " + userId));

        if (!film.getLikes().contains(userId)) {
            log.warn("Лайк пользователя с id={} не найден у фильма id={}", userId, filmId);
            throw new NotFoundException("Лайк пользователя с id=" + userId + " не найден у фильма id=" + filmId);
        }

        film.deleteUserIdFromFilmLikes(user.getId());
        log.info("Пользователь id={} убрал лайк с фильма id={}", userId, filmId);
        return film;
    }
}
