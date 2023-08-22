package ru.yandex.practicum.filmorate.exceptions;

public class EmptyFilmNameException extends ValidationException {
    public EmptyFilmNameException(String message) {
        super(message);
    }
}
