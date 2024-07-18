package ru.yandex.practicum.filmorate.storage.mappers.reviews;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getInt("ispositive") == 1)
                .useful(rs.getInt("ld"))
                .build();
    }
}
