package ru.yandex.practicum.filmorate.exceptions;

public class TooLongFilmDescriptionException extends ValidationException {
    public TooLongFilmDescriptionException(String message) {
        super(message);
    }
}
