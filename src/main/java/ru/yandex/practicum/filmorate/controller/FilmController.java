package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static ru.yandex.practicum.filmorate.validators.FilmValidator.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Получен запрос на вывод всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма");
        try {
            if (isFilmInfoValid(film)) {
                film.setId(getNextFilmId());
                films.put(film.getId(), film);
                log.info("Фильм создан: {}", film);
            }
        } catch (ValidationException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        log.info("Получен запрос на обновление фильма");
        //пустое поле при парсинге для примитива устанавливается 0,
        // проверяем это вместо null (id в программе начинаются с 1)
        if (updatedFilm.getId() == 0) {
            log.warn("Не указан id фильма для обновления");
            throw new ValidationException("Id должен быть указан.");
        }

        Film oldFilm = films.get(updatedFilm.getId());
        if (oldFilm == null) {
            log.warn("Фильм с id {} не найден", updatedFilm.getId());
            throw new NotFoundException("Фильм с указанным id = " + updatedFilm.getId() + " не найден.");
        }

        if (updatedFilm.getName() != null && !updatedFilm.getName().isEmpty()) {
            oldFilm.setName(updatedFilm.getName());
            log.info("Название фильма обновлено на {}", oldFilm.getName());
        }

        if (updatedFilm.getDescription() != null && !updatedFilm.getDescription().isEmpty() &&
                updatedFilm.getDescription().length() < MAX_DESCRIPTION_LENGTH) {
            oldFilm.setDescription(updatedFilm.getDescription());
            log.info("Описание фильма обновлено на {}", oldFilm.getDescription());
        }

        if (updatedFilm.getReleaseDate() != null &&
                !updatedFilm.getReleaseDate().isBefore(THE_BIRTH_OF_CINEMA)) {
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
            log.info("Дата выхода фильма обновлена на {}", oldFilm.getReleaseDate());
        }

        if (updatedFilm.getDuration() != null && updatedFilm.getDuration().isPositive()) {
            oldFilm.setDuration(updatedFilm.getDuration());
            log.info("Продолжитель фильма обновлена на {}", oldFilm.getDuration());
        }

        films.put(oldFilm.getId(), oldFilm);
        log.info("Обновленный фильм {} сохранен", oldFilm);
        return oldFilm;
    }

    private long getNextFilmId() {
        if (films.isEmpty()) {
            return 1;
        }
        Long currentMaxId = Collections.max(films.keySet());
        return ++currentMaxId;
    }
}
