package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;


    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


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
        if (likesId.contains(userId)) {
            throw new ValidationException("You're already likes this film");
        }
        likesId.add(userId);
        filmStorage.updateFilm(new Film(filmToPut.getId(), filmToPut.getName(),
                filmToPut.getDescription(), filmToPut.getReleaseDate(), filmToPut.getDuration(),
                likesId, filmToPut.getGenres(), filmToPut.getRating()));
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(long filmId, long userId) {
        Film filmToPut = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        Set<Long> likesId = filmToPut.getLikesId();
        if (!likesId.contains(userId)) {
            throw new ValidationException("You're not likes this film anyway");
        }
        likesId.remove(userId);
        filmStorage.updateFilm(new Film(filmToPut.getId(), filmToPut.getName(),
                filmToPut.getDescription(), filmToPut.getReleaseDate(), filmToPut.getDuration(),
                likesId, filmToPut.getGenres(), filmToPut.getRating()));
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getSortedFilmsList(long count) {
        if (count <= 0) {
            throw new ValidationException("Count must be positive");
        }
        return filmStorage.getSortedFilmsLst().stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
