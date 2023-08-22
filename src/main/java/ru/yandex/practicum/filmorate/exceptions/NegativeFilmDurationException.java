package ru.yandex.practicum.filmorate.exceptions;

public class NegativeFilmDurationException extends ValidationException {
    public NegativeFilmDurationException(String message) {
        super(message);
    }
}


