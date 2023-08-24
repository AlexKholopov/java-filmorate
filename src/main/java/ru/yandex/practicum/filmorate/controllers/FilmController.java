package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;

    @GetMapping
    public List<Film> getFilms() {
        log.info("getFilms is running");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("addFilm is running");
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
    public Film replaceFilm(@Valid @RequestBody Film film) {
        log.info("replaceFilm is running");
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

}
