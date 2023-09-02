package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.SearchedObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private Long idCounter = 1L;
    private final Map<Long, Film> films = new HashMap<>();
    private final Set<Film> sortedFilms = new TreeSet<>((f1, f2) -> {
        if (f1.getLikesId().size() == f2.getLikesId().size()) {
            return String.CASE_INSENSITIVE_ORDER.compare(f1.getName(), f2.getName());
        } else {
            return -1 * (f1.getLikesId().size() - f2.getLikesId().size());
        }
    });

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            Film film = films.get(id);
            return new Film(film.getId(), film.getName(),
                    film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getLikesId());
        } else {
            throw new SearchedObjectNotFoundException("Film with id = " + id + " not found");
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getSortedFilmsLst() {
        return new ArrayList<>(sortedFilms);
    }

    @Override
    public Film createFilm(Film film) {
        Film filmToAdd = Film.builder()
                .id(idCounter++)
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .likesId(film.getLikesId())
                .build();
        films.put(filmToAdd.getId(), filmToAdd);
        sortedFilms.add(filmToAdd);
        return filmToAdd;
    }

    @Override
    public Film updateFilm(Film film) {
        Film filmToAdd = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .likesId(film.getLikesId())
                .build();
        sortedFilms.remove(films.get(film.getId()));
        films.replace(film.getId(), filmToAdd);
        sortedFilms.add(filmToAdd);
        return filmToAdd;
    }
}
