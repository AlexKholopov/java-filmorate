package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Value
@EqualsAndHashCode
public class User {
    public User(long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.isEmpty() || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
        this.friends = Objects.requireNonNullElseGet(friends, HashSet::new);
    }

    long id;
    @Email(message = "must be a well-formed email address")
    String email;
    @NotBlank
    @Pattern(message = "must not be blank, must not contain whitespaces",
            regexp = "^[A-Za-z0-9_]+$")
    String login;
    String name;
    @PastOrPresent(message = "must be a date in the past or in the present")
    LocalDate birthday;
    Set<Long> friends;

}
