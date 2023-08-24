package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 1;

    @GetMapping
    public List<User> getUsers() {
        log.info("getUsers is running");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("createUser is running");
        User userToAdd = new User(idCounter++, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        users.put(userToAdd.getId(), userToAdd);
        return userToAdd;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("updateUser is running");
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.info("User successfully updated");
            return user;
        } else {
            throw new ValidationException("User with this id not found");
        }
    }
}
