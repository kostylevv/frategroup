package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import static ru.yandex.practicum.filmorate.validators.FilmValidator.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

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

        // т.к. валидатор используется в методе создания, он не проверяет id, поэтому дополнительно проверяем:
        if (updatedFilm.getId() == null) {
            log.warn("Не указан id фильма для обновления");
            throw new ValidationException("Id должен быть указан.");
        }

        if (films.get(updatedFilm.getId()) == null) {
            log.warn("Фильм с id {} не найден", updatedFilm.getId());
            throw new NotFoundException("Фильм с указанным id = " + updatedFilm.getId() + " не найден.");
        }

        //здесь уже проверяем валидатором
        try {
            isFilmInfoValid(updatedFilm);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
            throw exception;
        }

        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Обновленный фильм {} сохранен", updatedFilm);
        return updatedFilm;
    }

    private Integer getNextFilmId() {
        return ++id;
    }
}
