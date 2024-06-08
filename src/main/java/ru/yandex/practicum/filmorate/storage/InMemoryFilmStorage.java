package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import static ru.yandex.practicum.filmorate.validators.FilmValidator.isFilmInfoValid;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
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

    @Override
    public Film updateFilm(Film updatedFilm) {
        if (updatedFilm.getId() == null) {
            log.warn("Не указан id фильма для обновления");
            throw new ValidationException("Id должен быть указан.");
        }

        if (films.get(updatedFilm.getId()) == null) {
            log.warn("Фильм с id {} не найден", updatedFilm.getId());
            throw new NotFoundException("Фильм с указанным id = " + updatedFilm.getId() + " не найден.");
        }

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
