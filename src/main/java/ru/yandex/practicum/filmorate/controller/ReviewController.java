package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.reviews.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.reviews.ReviewDto;
import ru.yandex.practicum.filmorate.dto.reviews.UpdatedReviewRequest;
import ru.yandex.practicum.filmorate.service.reviews.ReviewService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto createReview(@Valid @RequestBody NewReviewRequest request) {
        return reviewService.createReview(request);
    }

    @PutMapping
    public ReviewDto updateReview(@Valid @RequestBody UpdatedReviewRequest request) {
        return reviewService.updateReview(request);
    }

    @GetMapping
    public List<ReviewDto> getReviewsForFilm(@RequestParam(required = true) int filmId) {
        return reviewService.getReviewsByFilmId(filmId);
    }

    @GetMapping("/{id}")
    public ReviewDto getReview(@PathVariable int id) {
        return reviewService.getReviewById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        reviewService.addLikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        reviewService.removeLikeFromReview(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        reviewService.addDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        reviewService.removeDislikeFromReview(reviewId, userId);
    }

}
