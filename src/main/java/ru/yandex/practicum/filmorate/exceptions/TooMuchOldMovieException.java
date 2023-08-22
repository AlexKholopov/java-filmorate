package ru.yandex.practicum.filmorate.exceptions;

public class TooMuchOldMovieException extends ValidationException {
    public TooMuchOldMovieException(String message) {
        super(message);
    }
}
