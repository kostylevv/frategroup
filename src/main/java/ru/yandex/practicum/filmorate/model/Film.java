package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.serialization.DurationDeserializer;
import ru.yandex.practicum.filmorate.serialization.DurationSerializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateSerializer;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private Set<Integer> likes;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;

    @JsonDeserialize(using = DurationDeserializer.class)
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    public Film(Integer id, String name, String description, Set<Integer> likes, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.likes = new HashSet<>();
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public void addUserIdToFilmLikes(Integer id) {
        likes.add(id);
    }

    public void deleteUserIdFromFilmLikes(Integer id) {
        likes.remove(id);
    }
}


