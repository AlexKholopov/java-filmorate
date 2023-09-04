package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUserFriends(long userId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("you can't friend yourself");
        }
        userStorage.getUserById(friendId).getFriends().add(userId);
        userStorage.getUserById(userId).getFriends().add(friendId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("you can't unfriend yourself");
        }
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        return userStorage.getUserById(userId);
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        return userStorage.getUserById(user1Id).getFriends().stream()
                .filter((l) -> userStorage.getUserById(user2Id).getFriends().contains(l))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
