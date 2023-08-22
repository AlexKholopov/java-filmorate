package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class User {
    int id;
    @Email
    String email;
    String login;
    String name;
    LocalDate birthday;
}
