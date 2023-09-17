package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
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
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(long filmId, long userId) {
        Film filmToPut = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        Set<Long> likesId = filmToPut.getLikesId();
        likesId.add(userId);
        filmStorage.updateFilm(new Film(filmToPut.getId(), filmToPut.getTitle(), filmToPut.getDescription(),
                filmToPut.getReleaseDate(), filmToPut.getDuration(), likesId, Genre.ACTION, Rating.R));
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(long filmId, long userId) {
        Film filmToDelete = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
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
}
