package ru.yandex.practicum.filmorate.dto.reviews;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class UpdatedReviewRequest {
    @Positive
    @JsonProperty("reviewId")
    private int id;

    @Positive
    private int userId;

    @Positive
    private int filmId;

    @Size(min = 1, max = 255, message
            = "Review content must be between 1 and 255 characters")
    private String content;

    @PositiveOrZero
    private int isPositive;
}
