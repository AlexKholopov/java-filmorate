package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectUserEmailException extends ValidationException {

    public IncorrectUserEmailException(String message) {
        super(message);
    }
}
