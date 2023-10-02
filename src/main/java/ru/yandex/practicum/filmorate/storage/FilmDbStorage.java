package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilmById(Long id) {
        String sql = "select * from films where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, RowNum) -> getFilm(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new SearchedObjectNotFoundException("Film with id = " + id + " not found");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, (rs, RowNum) -> getFilm(rs));
    }

    @Override
    public List<Film> getSortedFilmsLst() {
        String sql = "SELECT f.id  FROM films AS f LEFT JOIN likes AS l ON f.id = l.film_id " +
                "GROUP BY f.ID ORDER BY COUNT(l.USER_ID) DESC";
        return jdbcTemplate.query(sql, (rs, RowNum) -> rs.getLong("id")).stream()
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (title, description, duration, release_date, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setInt(3, film.getDuration());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        long id;
        try {
            id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (NullPointerException e) {
            throw new RuntimeException("Server error. Film wasn't made");
        }
        updateGenre(id, film.getGenres());
        return getFilmById(id);
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        String sql = "UPDATE films SET title = ?, description = ?, duration = ?, release_date = ?, " +
                "rating_id = ? WHERE id = ? ";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setInt(3, film.getDuration());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setInt(5, film.getMpa().getId());
            ps.setLong(6, film.getId());
            return ps;
        });
        updateLikes(film);
        updateGenre(film.getId(), film.getGenres());
        return getFilmById(film.getId());
    }


    private void updateGenre(long filmId, Set<Genre> genres) {
        jdbcTemplate.execute("DELETE FROM films_genre WHERE film_id = " + filmId);
        if (genres.isEmpty()) {
            return;
        }
        try {
            StringBuilder sqlGenresBuilder = new StringBuilder("INSERT INTO films_genre (film_id, genre_id) VALUES ");
            for (Genre genre : genres) {
                sqlGenresBuilder.append("(")
                        .append(filmId)
                        .append(", ")
                        .append(genre.getId())
                        .append("), ");
            }
            String sqlGenres = sqlGenresBuilder.toString();
            sqlGenres = sqlGenres.substring(0, sqlGenres.lastIndexOf(','));
            jdbcTemplate.execute(sqlGenres);
        } catch (Exception e) {
            throw new SearchedObjectNotFoundException("No such genre was found");
        }
    }

    private void updateLikes(Film film) {
        jdbcTemplate.execute("DELETE FROM likes where film_id = " + film.getId());
        if (film.getLikesId().isEmpty()) {
            return;
        }
        try {
            StringBuilder sqlLikes = new StringBuilder("INSERT INTO likes (film_id, user_id) VALUES ");
            for (Long l : film.getLikesId()) {
                sqlLikes.append("(")
                        .append(film.getId())
                        .append(", ")
                        .append(l)
                        .append("), ");
            }
            String likes = sqlLikes.toString();
            likes = likes.substring(0, likes.lastIndexOf(','));
            jdbcTemplate.execute(likes);
        } catch (Exception e) {
            throw new SearchedObjectNotFoundException("No such user was found");
        }
    }

    private Film getFilm(ResultSet rs) throws SQLException {
        Set<Long> likes = new HashSet<>(jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id = ?",
                (rsl, RowNum) -> rsl.getLong("user_id"), rs.getString("id")));

        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(
                "SELECT * FROM films_genre AS fg LEFT JOIN genre AS g ON fg.genre_id = g.id" +
                        " WHERE fg.film_id = ? ORDER BY fg.genre_id",
                (rsg, RowNum) -> getGenre(rsg), rs.getLong("id")));

        Rating rating = jdbcTemplate.queryForObject("SELECT * FROM rating WHERE id = ?",
                (rsr, RowNum) -> getRating(rsr), rs.getInt("rating_id"));

        Film film = new Film(rs.getLong("id"), rs.getString("title"),
                rs.getString("description"), rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"), likes, genres, rating);
        System.out.println(film);

        return film;
    }

    private Genre getGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

    private Rating getRating(ResultSet rs) throws SQLException {
        return new Rating(rs.getInt("id"), rs.getString("name"));
    }
}
