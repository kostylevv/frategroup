package ru.yandex.practicum.filmorate.storage.mappers.reviews;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReviewListResultSetExtractor implements ResultSetExtractor<List<Review>> {
    @Override
    public List<Review> extractData(ResultSet rs) throws SQLException, DataAccessException {
        return List.of();
    }
}
