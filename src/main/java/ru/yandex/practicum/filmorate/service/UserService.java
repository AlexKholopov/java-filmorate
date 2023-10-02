package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
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
        userStorage.getUserById(user.getId());
        return userStorage.updateUser(user);
    }

    public User addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("you can't friend yourself");
        }
        userStorage.getUserById(friendId);
        User user = userStorage.getUserById(userId);
        if (user.getFriends().contains(friendId)) {
            throw new ValidationException("You already friend with " + friendId);
        } else {
            Set<Long> friends = user.getFriends();
            friends.add(friendId);
            return userStorage.updateUser(new User(user.getId(), user.getEmail(),
                    user.getLogin(), user.getName(), user.getBirthday(), friends));
        }
    }

    public User deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("You can't unfriend yourself");
        }
        userStorage.getUserById(friendId);
        User user = userStorage.getUserById(userId);
        if (!user.getFriends().contains(friendId)) {
            throw new ValidationException("You're not friends anyway");
        }
        Set<Long> friends = user.getFriends();
        friends.remove(friendId);
        return userStorage.updateUser(new User(user.getId(), user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday(), friends));
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        return userStorage.getUserById(user1Id).getFriends().stream()
                .filter((l) -> userStorage.getUserById(user2Id).getFriends().contains(l))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
