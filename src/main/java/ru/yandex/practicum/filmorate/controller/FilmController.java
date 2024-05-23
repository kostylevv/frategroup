package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Instant theBirthOfCinema = Instant.parse("1895-12-28T00:00:00Z");
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        try {
            if (isFilmInfoValid(film)) {
                film.setId(getNextFilmId());
                films.put(film.getId(), film);
            }
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film updateUser(@RequestBody Film updatedFilm) {
        if (updatedFilm.getId() == 0) {
            throw new ValidationException("Id должен быть указан.");
        }

        Film oldFilm = films.get(updatedFilm.getId());
        if (oldFilm == null) {
            throw new NotFoundException("Фильм с указанным id = " + updatedFilm.getId() + " не найден.");
        }

        if (updatedFilm.getName() != null && !updatedFilm.getName().isEmpty()) {
            oldFilm.setName(updatedFilm.getName());
        }

        if (updatedFilm.getDescription() != null && !updatedFilm.getDescription().isEmpty() &&
                updatedFilm.getDescription().length() < MAX_DESCRIPTION_LENGTH) {
            oldFilm.setDescription(updatedFilm.getDescription());
        }

        if (updatedFilm.getReleaseDate() != null && updatedFilm.getReleaseDate().isBefore(theBirthOfCinema)) {
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
        }

        if (updatedFilm.getDuration() != null && updatedFilm.getDuration().isPositive()) {
            oldFilm.setDuration(updatedFilm.getDuration());
        }

        films.put(oldFilm.getId(), oldFilm);
        return oldFilm;
    }


    private boolean isFilmInfoValid(Film film) throws ValidationException {
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");

        } else if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");

        } else if (film.getReleaseDate().isBefore(theBirthOfCinema)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года;");

        } else if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
        return true;
    }

    private int getNextFilmId() {
        if (films.isEmpty()) {
            return 1;
        }
        int currentMaxId = Collections.max(films.keySet());
        return ++currentMaxId;
    }
}
