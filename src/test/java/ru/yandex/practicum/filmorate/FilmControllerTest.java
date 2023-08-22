package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.EmptyFilmNameException;
import ru.yandex.practicum.filmorate.exceptions.NegativeFilmDurationException;
import ru.yandex.practicum.filmorate.exceptions.TooLongFilmDescriptionException;
import ru.yandex.practicum.filmorate.exceptions.TooMuchOldMovieException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private final FilmController controller = new FilmController();
    private final Film emptyNameFilm = Film.builder()
            .releaseDate(LocalDate.of(1999, 12 , 5))
            .description("EmptyNameFilm")
            .id(1)
            .duration(190)
            .build();
    private final Film negativeDurationFilm = Film.builder()
            .name("Name")
            .releaseDate(LocalDate.of(1999, 12 , 5))
            .description("NegativeDurationFilm")
            .id(1)
            .duration(-5)
            .build();

    private final Film tooMuchOldFilm = Film.builder()
            .name("Name")
            .releaseDate(LocalDate.of(1850, 12 , 5))
            .description("TooMuchOldFilm")
            .id(1)
            .duration(190)
            .build();



    @Test
    public void shouldThrowEmptyFilmNameException() {
        //when
        final EmptyFilmNameException exception = assertThrows(
                EmptyFilmNameException.class,
                () -> controller.addFilm(emptyNameFilm));
        assertEquals("Validation exception: The title of the movie should not be blank",
                exception.getMessage());
    }

    @Test
    public void shouldThrowNegativeFilmDurationException() {
        NegativeFilmDurationException exception = assertThrows(
                NegativeFilmDurationException.class,
                () -> controller.addFilm(negativeDurationFilm));
        assertEquals("Validation exception: The duration of the movie should be positive",
                exception.getMessage());
    }

    @Test
    public void shouldThrowTooLongFilmDescription() {
        StringBuilder longDescription = new StringBuilder("description");
        while (longDescription.toString().length()<200) {
            longDescription.append(longDescription);
        }
        final Film tooLongDescriptionFilm = Film.builder()
                .name("Name")
                .releaseDate(LocalDate.of(1999, 12 , 5))
                .description(longDescription.toString())
                .id(1)
                .duration(190)
                .build();
        TooLongFilmDescriptionException exception = assertThrows(
                TooLongFilmDescriptionException.class,
                () -> controller.addFilm(tooLongDescriptionFilm));
        assertEquals("Validation exception: Movie description should be no longer than 200 characters",
                exception.getMessage());
    }

    @Test
    public void shouldThrowTooMuchOldMovieException() {
        TooMuchOldMovieException exception = assertThrows(
                TooMuchOldMovieException.class,
                () -> controller.addFilm(tooMuchOldFilm));
        assertEquals("Validation exception: The release date must not be earlier than December 28, 1895",
                exception.getMessage());
    }
}
