package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.serialization.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.serialization.LocalDateSerializer;
import java.time.LocalDate;

/**
 * User.
 */
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;
}

