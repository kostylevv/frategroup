package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
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
    private ReviewService service;

    @PostMapping
    public ReviewDto createReview(@RequestBody NewReviewRequest request) {
        return service.createReview(request);
    }

    @PutMapping
    public ReviewDto updateReview(@RequestBody UpdatedReviewRequest request) {
        return service.updateReview(request);
    }

    @GetMapping
    public List<ReviewDto> getReviewsForFilm(@RequestParam(required = true) int filmId) {
        return service.getReviewsByFilmId(filmId);
    }

    @GetMapping("/{id}")
    public ReviewDto getReview(@PathVariable int id) {
        return service.getReviewById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        service.deleteReview(id);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        service.addLikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        service.removeLikeFromReview(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        service.addDislikeToReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable("reviewId") int reviewId, @PathVariable int userId) {
        service.removeDislikeFromReview(reviewId, userId);
    }

}
