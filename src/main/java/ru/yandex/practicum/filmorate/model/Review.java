package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    private int userId;
    private int filmId;
    private String content;
    private boolean positive;
    private int useful; //difference between likes and dislikes
}
