package ru.yandex.practicum.filmorate.dto.reviews;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDto {
    private int userId;
    private int filmId;
    private String content;
    private int isPositive;
    private int useful; //how many times review was liked
}
