package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private ValidatingService service;
    private final Film emptyNameFilm = new Film(1L, "", "EmptyNameFilm",
            LocalDate.of(1999, 12, 5), 190, Collections.emptySet(), Set.of(new Genre(1)),
            new Rating(2));
    private final Film negativeDurationFilm = new Film(1L, "Name", "NegativeDurationFilm",
            LocalDate.of(1999, 12, 5), -5, Collections.emptySet(), Set.of(new Genre(1)),
            new Rating(2));

    private final Film tooMuchOldFilm = new Film(1L, "Name", "TooMuchOldFilm",
            LocalDate.of(1850, 12, 5), 190, Collections.emptySet(), Set.of(new Genre(1)),
            new Rating(2));



    @Test
    public void shouldThrowEmptyFilmNameException() {
        //when
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> service.validateInputWithInjectedValidator(emptyNameFilm));
        assertEquals("name: must not be blank",
                exception.getMessage());
    }

    @Test
    public void shouldThrowNegativeFilmDurationException() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> service.validateInputWithInjectedValidator(negativeDurationFilm));
        assertEquals("duration: must be greater than or equal to 0",
                exception.getMessage());
    }

    @Test
    public void shouldThrowTooLongFilmDescription() {
        StringBuilder longDescription = new StringBuilder("description");
        while (longDescription.toString().length() < 200) {
            longDescription.append(longDescription);
        }
        final Film tooLongDescriptionFilm = new Film(1L, "Name", longDescription.toString(),
                LocalDate.of(1999, 12, 5), 190, Collections.emptySet(), Set.of(new Genre(1)),
                new Rating(2));
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> service.validateInputWithInjectedValidator(tooLongDescriptionFilm));
        assertEquals("description: size must be between 0 and 200",
                exception.getMessage());
    }

    @Test
    public void shouldThrowTooMuchOldMovieException() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> service.validateInputWithInjectedValidator(tooMuchOldFilm));
        assertEquals("releaseDate: Validation exception: The release date must not be earlier than December 28, 1895",
                exception.getMessage());
    }


}
