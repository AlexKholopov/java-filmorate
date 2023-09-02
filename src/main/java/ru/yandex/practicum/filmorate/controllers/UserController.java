package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("getUsers is running");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@Valid @PathVariable long id) {
        log.info("Running getUserById with id = " + id);
        return userService.getUserById(id);
    }

    @GetMapping("{id}/friends")
    public List<User> getUsersFriends(@Valid @PathVariable long id) {
        log.info("Running getUsersFriends with user id = " + id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@Valid @PathVariable long id, @PathVariable long otherId) {
        log.info("Running get common friends with user 1 id = " + id + ", user 2 id = " + otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("createUser is running");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("updateUser is running");
        return userService.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(@Valid @PathVariable long id, @PathVariable long friendId) {
        log.info("Running addFriend with user 1 id = " + id + ", user 2 id = " + friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User deleteFriend(@Valid @PathVariable long id, @PathVariable long friendId) {
        log.info("Running deleteFriend with user 1 id = " + id + ", user 2 id = " + friendId);
        return userService.deleteFriend(id, friendId);
    }
}
