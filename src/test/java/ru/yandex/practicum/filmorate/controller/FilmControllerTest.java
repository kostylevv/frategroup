package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void findAllFilms_shouldReturnAllFilms() {
        Film film1 = new Film(1, "Film1", "Description1",
                LocalDate.of(2000, 1, 1), 100);
        Film film2 = new Film(2, "Film2", "Description2",
                LocalDate.of(2000, 1, 1), 100);
        filmController.films.put(1L, film1);
        filmController.films.put(2L, film2);

        Collection<Film> allFilms = filmController.findAllFilms();

        assertEquals(2, allFilms.size());
    }

    @Test
    void createFilm_shouldAddNewFilm() {
        Film film = new Film(0, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);

        Film createdFilm = filmController.createFilm(film);

        assertEquals(1, filmController.films.size());
        assertEquals(film.getName(), createdFilm.getName());
    }

    @Test
    void createFilm_shouldSetIdInOrder() {
        Film film1 = new Film(0, "Film1", "Description1",
                LocalDate.of(2000, 1, 1), 100);
        Film film2 = new Film(0, "Film2", "Description2",
                LocalDate.of(2000, 1, 1), 100);
        Film film3 = new Film(0, "Film3", "Description3",
                LocalDate.of(2000, 1, 1), 100);

        Film createdFilm1 = filmController.createFilm(film1);
        Film createdFilm2 = filmController.createFilm(film2);
        Film createdFilm3 = filmController.createFilm(film3);

        assertEquals(3, filmController.films.size());
        assertEquals(1, createdFilm1.getId());
        assertEquals(2, createdFilm2.getId());
        assertEquals(3, createdFilm3.getId());
    }

    @Test
    void updateFilm_shouldUpdateFilmWhenAllFieldsAreCorrect() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmController.films.put(1L, film);
        Film updatedFilm = new Film(1, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        filmController.updateFilm(updatedFilm);

        Film updatedActual = filmController.films.get(1L);
        assertEquals("FilmUpdated", updatedActual.getName());
        assertEquals("DescriptionUpdated", updatedActual.getDescription());
        assertEquals(LocalDate.of(2005, 5, 5), updatedActual.getReleaseDate());
        assertEquals(50, updatedActual.getDuration().toMinutes());
    }

    @Test
    void updateFilm_shouldThrowValidationExceptionWhenIdIs0() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmController.films.put(1L, film);
        Film updatedFilm = new Film(0, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        Assertions.assertThrows(ValidationException.class, () -> filmController.updateFilm(updatedFilm));
    }

    @Test
    void updateFilm_shouldThrowNotFoundExceptionWhenIdNonExistent() {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(2000, 1, 1), 100);
        filmController.films.put(1L, film);
        Film updatedFilm = new Film(7, "FilmUpdated", "DescriptionUpdated",
                LocalDate.of(2005, 5, 5), 50);

        Assertions.assertThrows(NotFoundException.class, () -> filmController.updateFilm(updatedFilm));
    }
}
