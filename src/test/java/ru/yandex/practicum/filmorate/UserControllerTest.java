package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private ValidatingService service;

    private final User wrongBirthdayUser = new User(1L, "asd@mail.com", "login",
            "Name", LocalDate.of(2025, 12, 31), Collections.emptySet());

    private final User wrongEmailUser = new User(1L, "asdmail.com", "login",
            "Name", LocalDate.of(1999, 12, 31), Collections.emptySet());

    private final User wrongLoginUser = new User(1L, "asd@mail.com", "log in",
            "Name", LocalDate.of(1999, 12, 31), Collections.emptySet());

    @Test
    public void shouldThrowIncorrectBirthdayException() {
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> service.validateInputWithInjectedValidator(wrongBirthdayUser)
        );
        assertEquals("birthday: must be a date in the past or in the present",
                exception.getMessage());
    }

    @Test
    public void shouldThrowIncorrectUserEmailException() {
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> service.validateInputWithInjectedValidator(wrongEmailUser)
        );
        assertEquals("email: must be a well-formed email address",
                exception.getMessage());
    }

    @Test
    public void shouldThrowIncorrectLoginException() {
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> service.validateInputWithInjectedValidator(wrongLoginUser)
        );
        assertEquals("login: must not be blank, must not contain whitespaces",
                exception.getMessage());
    }
}
