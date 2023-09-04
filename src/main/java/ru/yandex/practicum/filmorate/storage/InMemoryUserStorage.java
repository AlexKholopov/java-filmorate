package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            User user = users.get(id);
            return new User(user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getFriends());
        } else {
            throw new SearchedObjectNotFoundException("User with id = " + id + " not found");
        }
    }

    @Override
    public User createUser(User user) {
        User userToAdd = new User(idCounter++, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getFriends());
        users.put(userToAdd.getId(), userToAdd);
        return userToAdd;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            return user;
        } else {
            throw new SearchedObjectNotFoundException("User with id = " + user.getId() + " not found");
        }
    }
}
