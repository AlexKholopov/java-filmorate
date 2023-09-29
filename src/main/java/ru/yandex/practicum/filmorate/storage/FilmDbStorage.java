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
        Film film1 = new Film(id, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getLikesId(), film.getGenres(), film.getMpa());
        updateGenre(film1);
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
        updateGenre(film);
        return getFilmById(film.getId());
    }

    /*
    Я очень долго пытался найти нормальный способ добавить жанры, но так и не нашел
    Если я правильно понимаю, то в Н2 нет синтаксиса ON CONFLICT
    И поэтому приходится полностью всё удалять и заполнять по новой
    Если есть какой-то адекватный вариант как это сделать я с радостью, но к сожалению сам не нашел...
     */
    private void updateGenre(Film film) {
        jdbcTemplate.execute("DELETE FROM films_genre WHERE film_id = " + film.getId());
        if (film.getGenres().isEmpty()) {
            return;
        }
        try {
            StringBuilder sqlGenres = new StringBuilder("INSERT INTO films_genre (film_id, genre_id) VALUES ");
            for (Genre genre : film.getGenres()) {
                sqlGenres.append("(")
                        .append(film.getId())
                        .append(", ")
                        .append(genre.getId())
                        .append("), ");
            }
            String genres = sqlGenres.toString();
            genres = genres.substring(0, genres.lastIndexOf(','));
            jdbcTemplate.execute(genres);
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
                "SELECT genre_id FROM films_genre WHERE film_id = ? ORDER BY genre_id",
                (rsg, RowNum) -> getGenre(rsg), rs.getLong("id")));

        Rating rating = new Rating(rs.getInt("rating_id"));

        return new Film(rs.getLong("id"), rs.getString("title"),
                rs.getString("description"), rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"), likes, genres, rating);
    }

    private Genre getGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"));
    }
}
