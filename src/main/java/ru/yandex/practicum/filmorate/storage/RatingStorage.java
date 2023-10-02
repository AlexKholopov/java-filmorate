package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingStorage {

    Rating getRatingById(Integer id);

    List<Rating> getRatings();
}
