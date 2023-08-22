package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectLoginException extends ValidationException {
    public IncorrectLoginException(String message) {
        super(message);
    }
}
