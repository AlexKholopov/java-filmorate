package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Rating getRatingById(Integer id) {
        String sql = "SELECT * FROM rating WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, RowNum) -> getRating(rs), id);
        } catch (Exception e) {
            throw new SearchedObjectNotFoundException("Rating with id = " + id + " not found");
        }
    }

    @Override
    public List<Rating> getRatings() {
        String sql = "SELECT * FROM rating ORDER BY id";
        return jdbcTemplate.query(sql, (rs, RowNum) -> getRating(rs));
    }

    private Rating getRating(ResultSet rs) throws SQLException {
        return new Rating(rs.getInt("id"), rs.getString("name"));
    }
}
