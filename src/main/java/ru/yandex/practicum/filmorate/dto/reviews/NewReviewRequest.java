package ru.yandex.practicum.filmorate.dto.reviews;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class NewReviewRequest {
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
