package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private ValidatingService service;
    private final Film emptyNameFilm = Film.builder()
            .releaseDate(LocalDate.of(1999, 12, 5))
            .description("EmptyNameFilm")
            .id(1)
            .duration(190)
            .likesId(null)
            .build();
    private final Film negativeDurationFilm = Film.builder()
            .title("Name")
            .releaseDate(LocalDate.of(1999, 12, 5))
            .description("NegativeDurationFilm")
            .id(1)
            .duration(-5)
            .likesId(null)
            .build();

    private final Film tooMuchOldFilm = Film.builder()
            .title("Name")
            .releaseDate(LocalDate.of(1850, 12, 5))
            .description("TooMuchOldFilm")
            .id(1)
            .duration(190)
            .likesId(null)
            .build();



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
        final Film tooLongDescriptionFilm = Film.builder()
                .title("Name")
                .releaseDate(LocalDate.of(1999, 12, 5))
                .description(longDescription.toString())
                .id(1)
                .duration(190)
                .build();
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
