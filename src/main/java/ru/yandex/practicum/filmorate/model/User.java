package ru.yandex.practicum.filmorate.model;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Value
public class User {
    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    int id;
    @Email
    String email;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_]+$") //надеюсь правильно) вроде \s ищет пробелы, но что с ними делает неясно
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;
}
