package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.Collection;

public interface GenreService {

    Collection<GenreDto> getAllGenres();

    GenreDto findGenreById(Integer id);
}
