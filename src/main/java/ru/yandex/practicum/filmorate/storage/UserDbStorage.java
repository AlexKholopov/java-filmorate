package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users ORDER BY id";
        return jdbcTemplate.query(sql, (rs, RowNum) -> getUser(rs));
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, RowNum) -> getUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new SearchedObjectNotFoundException("User with id = " + id + " not found");
        }
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            return ps;
        }, keyHolder);
        long id;
        try {
            id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (NullPointerException e) {
            throw new RuntimeException("Server error. Film wasn't made");
        }
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            ps.setLong(5, user.getId());
            return ps;
        });
        updateFriends(user);
        return getUserById(user.getId());
    }

    private void updateFriends(User user) {
        jdbcTemplate.execute("DELETE FROM friendship where user1_id = " + user.getId());
        if (user.getFriends().isEmpty()) {
            return;
        }
        StringBuilder sqlFriends = new StringBuilder("INSERT INTO friendship (user1_id, user2_id) VALUES ");
        for (Long l : user.getFriends()) {
            sqlFriends.append("(")
                    .append(user.getId())
                    .append(", ")
                    .append(l)
                    .append("), ");
        }
        String friends = sqlFriends.toString();
        friends = friends.substring(0, friends.lastIndexOf(','));
        System.out.println(friends);
        jdbcTemplate.execute(friends);
    }

    private User getUser(ResultSet rs) throws SQLException {
        Set<Long> friends = new HashSet<>(jdbcTemplate.query("select user2_id from friendship where user1_id = ?",
                (rsl, RowNum) -> rsl.getLong("user2_id"), rs.getString("id")));

        return new User(rs.getLong("id"), rs.getString("email"),
                rs.getString("login"), rs.getString("name"),
                rs.getDate("birthday").toLocalDate(), friends);
    }
}
