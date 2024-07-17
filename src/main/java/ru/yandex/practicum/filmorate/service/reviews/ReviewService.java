package ru.yandex.practicum.filmorate.service.reviews;

import ru.yandex.practicum.filmorate.dto.reviews.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.reviews.ReviewDto;
import ru.yandex.practicum.filmorate.dto.reviews.UpdatedReviewRequest;

import java.util.List;

public interface ReviewService {
    ReviewDto getReviewById(int id);
    List<ReviewDto> getReviewsByFilmId(int id);
    ReviewDto createReview(NewReviewRequest request);
    ReviewDto updateReview(UpdatedReviewRequest request);
    void addLikeToReview(int reviewId, int userId);
    void removeLikeFromReview(int reviewId, int userId);
    void addDislikeToReview(int reviewId, int userId);
    void removeDislikeFromReview(int reviewId, int userId);
    void deleteReview(int id);
}
