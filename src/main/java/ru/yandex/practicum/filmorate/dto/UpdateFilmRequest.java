package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.serialization.DurationSerializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateFilmRequest {
    private Integer id;
    private String name;
    private String description;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    Duration duration;
    private Set<Genre> genresId;
    private Mpa mpa;

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasGenres() {
        return genresId != null && !genresId.isEmpty();
    }

    public boolean hasMpa() {
        return mpa != null;
    }
}
