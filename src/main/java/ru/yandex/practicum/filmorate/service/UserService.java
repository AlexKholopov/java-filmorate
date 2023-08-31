package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUsers(){
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        validateUsers(id);
        return userStorage.getUserById(id);}

    public List<User> getUserFriends(long userId) {
        validateUsers(userId);
        List<User> usersFriends = new ArrayList<>();
        for (long id : userStorage.getUserById(userId).getFriends()) {
            usersFriends.add(userStorage.getUserById(id));
        }
        return usersFriends;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUsers(user.getId());
        return userStorage.updateUser(user);
    }

    public User addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("you can't friend yourself");
        }
        validateUsers(userId);
        validateUsers(friendId);
        userStorage.getUserById(userId).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(userId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("you can't unfriend yourself");
        }
        validateUsers(userId);
        validateUsers(friendId);
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        return userStorage.getUserById(userId);
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) {
        validateUsers(user1Id);
        validateUsers(user2Id);
        Set<Long> u1Friends = new HashSet<>(Set.copyOf(userStorage.getUserById(user1Id).getFriends()));
        u1Friends.retainAll(userStorage.getUserById(user2Id).getFriends());
        List<User> commonFriendsList = new ArrayList<>();
        for (Long l : u1Friends) {
            commonFriendsList.add(userStorage.getUserById(l));
        }
        return commonFriendsList;
    }

    private void validateUsers(long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new SearchedObjectNotFoundException("User with id = " + userId + " not found");
        }
    }
}
