package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;
    private final int MAX_DESCRIPTION_LENGTH = 200;
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getFilms() {
        log.debug("");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        Film filmToAdd = Film.builder()
                .id(idCounter++)
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        films.put(filmToAdd.getId(), filmToAdd);
        return filmToAdd;
    }

    @PutMapping
    public Film replaceFilm(@RequestBody Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Film with this id not found");
        }
        Film filmToAdd = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        films.replace(filmToAdd.getId(), filmToAdd);
        return filmToAdd;
    }


    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank() || film.getName().isEmpty()) {
            throw new EmptyFilmNameException("The title of the movie should not be blank");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new TooLongFilmDescriptionException("Movie description should be no longer than 200 characters");
        }
        if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            throw new TooMuchOldMovieException("The release date must not be earlier than December 28, 1895");
        }
        if (film.getDuration() <= 0) {
            throw new NegativeFilmDurationException("The duration of the movie should be positive");
        }
    }
}
