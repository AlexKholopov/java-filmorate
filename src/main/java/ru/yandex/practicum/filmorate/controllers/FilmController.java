package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("getFilms is running");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@Valid @PathVariable long id) {
        log.info("Running getFilmById with id =  " + id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getSortedFilmList(@Valid @RequestParam(name = "count",
            required = false, defaultValue = "10") long count) {
        log.info("Running getSortedFilmList with count = " +  count);
        return filmService.getSortedFilmsList(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Running createFilm");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Running updateFilm");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@Valid @PathVariable long id, @PathVariable long userId) {
        log.info("Running addLike film id = " + id + ", user id = " + userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@Valid @PathVariable long id, @PathVariable long userId) {
        log.info("Running deleteLike film id = " + id + ", user id = " + userId);
        return filmService.deleteLike(id, userId);
    }
}
