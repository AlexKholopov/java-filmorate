package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private ValidatingService service;

    private final User wrongBirthdayUser = new User(1, "asd@mail.com", "login",
            "Name", LocalDate.of(2025, 12, 31));

    private final User wrongEmailUser = new User(1, "asdmail.com", "login",
            "Name", LocalDate.of(1999, 12, 31));

    private final User wrongLoginUser = new User(1, "asd@mail.com", "log in",
            "Name", LocalDate.of(1999, 12, 31));

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
