package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectLoginException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserEmailException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int idCounter = 1;


    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        User userToAdd;
        if (user.getName() == null) {
            userToAdd = User.builder()
                    .id(idCounter++)
                    .login(user.getLogin())
                    .birthday(user.getBirthday())
                    .email(user.getEmail())
                    .name(user.getLogin())
                    .build();
        } else {
            userToAdd = User.builder()
                    .id(idCounter++)
                    .login(user.getLogin())
                    .birthday(user.getBirthday())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }
        users.put(userToAdd.getId(), userToAdd);
        return userToAdd;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user){
        if (users.containsKey(user.getId())) {
            validateUser(user);
            User userToAdd;
            if (user.getName() == null) {
                userToAdd = User.builder()
                        .id(user.getId())
                        .login(user.getLogin())
                        .birthday(user.getBirthday())
                        .email(user.getEmail())
                        .name(user.getLogin())
                        .build();
            } else {
                userToAdd = User.builder()
                        .id(user.getId())
                        .login(user.getLogin())
                        .birthday(user.getBirthday())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build();
            }
            users.replace(userToAdd.getId(), userToAdd);
            log.debug("User successfully updated");
            return userToAdd;
        }
        else {
            throw new ValidationException("User with this id not found");
        }
    }

    private void validateUser(User user) {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new IncorrectUserEmailException("Email cannot be empty and must contain the @ symbol");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new IncorrectLoginException("Login cannot be empty and cannot contain spaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new IncorrectBirthdayException("The date of birth can't be in the future");
        }
    }
}
