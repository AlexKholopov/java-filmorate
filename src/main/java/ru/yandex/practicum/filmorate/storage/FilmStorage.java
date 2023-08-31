package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film getFilmById(Long id);
    List<Film> getFilms();
    List<Film> getSortedFilmsLst();
    Film createFilm(Film film);
    Film updateFilm(Film film);
}
