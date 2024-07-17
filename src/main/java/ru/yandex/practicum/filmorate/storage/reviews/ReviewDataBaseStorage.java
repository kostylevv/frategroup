package ru.yandex.practicum.filmorate.storage.reviews;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDataBaseStorage extends BaseStorage<Review> implements ReviewStorage {
    /**
     * INSERT INTO app_user(email, login, name, birthday) VALUES ('b@b.com', 'l2', 'n2', '2000-01-02')
     * INSERT INTO film (name, description, release_date, duration, mpa_id) VALUES ('name1', 'd1', '2001-01-01', '1', 1);
     * INSERT INTO reviews (user_id, film_id, content, ispositive) VALUES (1, 1, 'good', 1);
     * INSERT INTO reviews (user_id, film_id, content, ispositive) VALUES (1, 2,  'not good', 0);
     * INSERT INTO reviews (user_id, film_id, content, ispositive) VALUES (2, 2, 'not good from user2', 0);
     * INSERT INTO reviews (user_id, film_id, content, ispositive) VALUES (3, 2, 'good', 1);
     * INSERT INTO reviews (user_id, film_id, content, ispositive) VALUES (3, 3, 'good', 1);
     */
    private static final String INSERT = "INSERT INTO reviews (user_id, film_id, content, ispositive) " +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE reviews SET content = ?, ispositive = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM reviews WHERE id = ?";

    private static final String GET_REVIEW_BY_ID = """
            SELECT r.*, SUM(rld.like_dislike) AS ld FROM REVIEWS AS r
            LEFT JOIN REVIEW_LIKES_DISLIKES AS rld
            ON rld.review_id = r.id
            GROUP BY r.id
            HAVING r.id = ?;
            """;
    private static final String GET_ALL_REVIEWS = """
            SELECT r.*, SUM(rld.like_dislike) AS ld FROM REVIEWS AS r
            LEFT JOIN REVIEW_LIKES_DISLIKES AS rld
            ON rld.review_id = r.id
            GROUP BY r.id;
            """;
    private static final String GET_ALL_REVIEWS_BY_FILM_ID = """
            SELECT r.*, SUM(rld.like_dislike) AS ld FROM REVIEWS AS r
            LEFT JOIN REVIEW_LIKES_DISLIKES AS rld
            ON rld.review_id = r.id
            GROUP BY r.id
            HAVING r.film_id = ?;
            """;

    public ReviewDataBaseStorage(JdbcTemplate jdbc, RowMapper<Review> mapper, ResultSetExtractor<List<Review>> listExtractor) {
        super(jdbc, mapper, listExtractor);
    }

    @Override
    public Optional<Review> getReviewById(int id) {
        return findOneMapper(GET_REVIEW_BY_ID, id);
    }

    @Override
    public List<Review> getReviewsByFilmId(int id) {
        return findManyExtractor(GET_ALL_REVIEWS_BY_FILM_ID, id);

    }

    @Override
    public Review createReview(Review review) {
        Integer id = insert(
                INSERT,
                review.getUserId(),
                review.getFilmId(),
                review.getContent(),
                review.isPositive() ? 1 : 0);
        review.setId(id);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        update(UPDATE,
                review.getContent(),
                review.isPositive() ? 1 : 0,
                review.getId());
        return review;
    }

    @Override
    public void addLikeToReview(int reviewId, int userId) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void removeLikeFromReview(int reviewId, int userId) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void addDislikeToReview(int reviewId, int userId) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void removeDislikeFromReview(int reviewId, int userId) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void deleteReview(int id) {
        delete(DELETE, id);
    }
}
