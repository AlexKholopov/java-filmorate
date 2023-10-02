package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genre ORDER BY id";
        return jdbcTemplate.query(sql, (rs, RowNum) -> getGenre(rs));
    }

    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM genre WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, RowNum) -> getGenre(rs), id);
        } catch (DataAccessException e) {
            throw new SearchedObjectNotFoundException("Genre with id = " + id + " not found");
        }
    }

    private Genre getGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }
}
