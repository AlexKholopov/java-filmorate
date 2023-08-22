package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.IncorrectBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectLoginException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserEmailException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private final UserController controller = new UserController();

    private final User wrongBirthdayUser = User.builder()
            .name("Name")
            .email("asd@mail.com")
            .id(1)
            .login("login")
            .birthday(LocalDate.of(2025, 12, 31))
            .build();
    private final User wrongEmailUser = User.builder()
            .name("Name")
            .email("asdmail.com")
            .id(1)
            .login("login")
            .birthday(LocalDate.of(1999, 12, 31))
            .build();

    private final User wrongLoginUser = User.builder()
            .name("Name")
            .email("asd@mail.com")
            .id(1)
            .login("log in")
            .birthday(LocalDate.of(1999, 12, 31))
            .build();

    @Test
    public void shouldThrowIncorrectBirthdayException() {
        IncorrectBirthdayException exception = assertThrows(
                IncorrectBirthdayException.class,
                () -> controller.createUser(wrongBirthdayUser)
        );
        assertEquals("Validation exception: The date of birth can't be in the future",
                exception.getMessage());
    }

    @Test
    public void shouldThrowIncorrectUserEmailException() {
        IncorrectUserEmailException exception = assertThrows(
                IncorrectUserEmailException.class,
                () -> controller.createUser(wrongEmailUser)
        );
        assertEquals("Validation exception: Email cannot be empty and must contain the @ symbol",
                exception.getMessage());
    }

    @Test
    public void shouldThrowIncorrectLoginException() {
        IncorrectLoginException exception = assertThrows(
                IncorrectLoginException.class,
                () -> controller.createUser(wrongLoginUser)
        );
        assertEquals("Validation exception: Login cannot be empty and cannot contain spaces",
                exception.getMessage());
    }
}
