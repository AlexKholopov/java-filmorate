package ru.yandex.practicum.filmorate.exceptions;

public class SearchedObjectNotFoundException extends RuntimeException {
    public SearchedObjectNotFoundException(String message) {
        super(message);
    }
}
