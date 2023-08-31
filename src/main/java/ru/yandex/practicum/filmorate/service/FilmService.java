package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilms(film.getId());
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(long id) {
        validateFilms(id);
        return filmStorage.getFilmById(id);
    }

    public Film addLike(long filmId, long userId) {
        validateFilms(filmId);
        validateUser(userId);
        Film filmToPut = filmStorage.getFilmById(filmId);
        Set<Long> likesId = filmToPut.getLikesId();
        likesId.add(userId);
        filmStorage.updateFilm(new Film(filmToPut.getId(), filmToPut.getName(), filmToPut.getDescription(),
                filmToPut.getReleaseDate(), filmToPut.getDuration(), likesId));
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(long filmId, long userId) {
        validateFilms(filmId);
        validateUser(userId);
        Film filmToDelete = filmStorage.getFilmById(filmId);
        filmStorage.getFilmById(filmId).getLikesId().remove(userId);
        filmStorage.updateFilm(filmToDelete);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getSortedFilmsList(long count) {
        if (count <= 0) {
            throw new ValidationException("Count must be positive");
        }
        return  filmStorage.getSortedFilmsLst().stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateFilms(long filmId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new SearchedObjectNotFoundException("Film with id = " + filmId + " not found");
        }
    }

    private void validateUser(long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new SearchedObjectNotFoundException("User with id = " + userId + " not found");
        }
    }
}
