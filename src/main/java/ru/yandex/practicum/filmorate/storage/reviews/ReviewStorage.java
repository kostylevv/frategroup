package ru.yandex.practicum.filmorate.storage.reviews;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review getReviewById(int id);
    List<Review> getReviewsByFilmId(int id);
    Review createReview(Review review);
    Review updateReview(Review review);
    void addLikeToReview(int reviewId, int userId);
    void removeLikeFromReview(int reviewId, int userId);
    void addDislikeToReview(int reviewId, int userId);
    void removeDislikeFromReview(int reviewId, int userId);
    void deleteReview(int id);
}
