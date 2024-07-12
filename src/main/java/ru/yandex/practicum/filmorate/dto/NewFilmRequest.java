package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.serialization.DurationDeserializer;
import ru.yandex.practicum.filmorate.serialization.DurationSerializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class NewFilmRequest {
    private String name;
    private String description;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;
    private Set<Genre> genres;
    private Mpa mpa;
}
