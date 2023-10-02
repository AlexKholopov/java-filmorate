package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmorateApplicationTest {
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    @Autowired
    public FilmorateApplicationTest(@Qualifier("userDbStorage") UserStorage userStorage,
                                    @Qualifier("filmDbStorage") FilmStorage filmStorage,
                                    GenreStorage genreStorage, RatingStorage ratingStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }

    @BeforeEach
    public void doBeforeEach() {

    }

    @Test
    public void userStorageTest() {

        User user = new User(1L, "asd@mail.com", "login",
                "Name", LocalDate.of(1998, 5, 3), Collections.emptySet());
        User user1 = new User(2L, "asd@mail.com", "login2",
                "Name2", LocalDate.of(1998, 5, 13), Collections.emptySet());


        userStorage.createUser(new User(1L, "asd@mail.com", "login",
                "Name", LocalDate.of(1998, 5, 3), Collections.emptySet()));
        userStorage.createUser(new User(2L, "asd@mail.com", "login2",
                "Name2", LocalDate.of(1998, 5, 13), Collections.emptySet()));


        User userToCheck = userStorage.getUserById(1L);
        assertEquals(user, userToCheck, "Wrong user upload");

        user = new User(1L, "asdNew@mail.com", "loginNew",
                "NameUploaded", LocalDate.of(1999, 5, 3), Set.of(2L));
        userStorage.updateUser(new User(1L, "asdNew@mail.com", "loginNew",
                "NameUploaded", LocalDate.of(1999, 5, 3), Set.of(2L)));


        userToCheck = userStorage.getUserById(1L);
        assertEquals(user, userToCheck, "Wrong user update");


        List<User> usersList = List.of(user, user1);
        assertArrayEquals(new List[]{usersList}, new List[]{userStorage.getUsers()}, "Wrong users list");
    }

    @Test
    public void filmStorageTest() {
        Film film = new Film(1L, "Name", "Description",
                LocalDate.of(1999, 12, 5), 190, Collections.emptySet(),
                Set.of(new Genre(1, "COMEDY")), new Rating(2, "PG"));
        Film film1 = new Film(2L, "Name2", "Description2",
                LocalDate.of(1999, 12, 5), 190, Collections.emptySet(),
                Set.of(new Genre(2, "DRAMA")), new Rating(3, "PG_13"));

        filmStorage.createFilm(new Film(1L, "Name", "Description",
                LocalDate.of(1999, 12, 5), 190, Collections.emptySet(),
                Set.of(new Genre(1, "COMEDY")), new Rating(2, "PG")));
        filmStorage.createFilm(new Film(2L, "Name2", "Description2",
                LocalDate.of(1999, 12, 5), 190, Collections.emptySet(),
                Set.of(new Genre(2, "DRAMA")), new Rating(3, "PG_13")));

        Film filmToCheck = filmStorage.getFilmById(1L);
        assertEquals(film, filmToCheck, "Wrong upload film");


        film = new Film(1L, "NameNew", "DescriptionNew",
                LocalDate.of(1999, 12, 15), 190, Collections.emptySet(),
                Set.of(new Genre(3, "CARTOON")), new Rating(4, "R"));
        filmStorage.updateFilm(film);

        filmToCheck = filmStorage.getFilmById(1L);
        assertEquals(film, filmToCheck, "Wrong film update");


        List<Film> filmsList = List.of(film, film1);
        assertArrayEquals(new List[]{filmsList}, new List[]{filmStorage.getFilms()}, "Wrong films list");
    }

    @Test
    public void genreStorageTest() {
        List<Genre> genreList = List.of(new Genre(1, "COMEDY"), new Genre(2, "DRAMA"), new Genre(3, "CARTOON"),
                new Genre(4, "THRILLER"), new Genre(5, "DOCUMENTARY"), new Genre(6, "ACTION"));
        assertAll("Genre check",
                () -> assertArrayEquals(new List[]{genreList}, new List[]{genreStorage.getGenres()}, "Wrong genre list"),
                () -> assertEquals(new Genre(1, "COMEDY"), genreStorage.getGenreById(1), "Wrong genre by id")
        );
    }

    @Test
    public void ratingStorageTest() {
        List<Rating> ratingList = List.of(new Rating(1, "G"), new Rating(2, "PG"),
                new Rating(3, "PG_13"), new Rating(4, "R"), new Rating(5, "NC_17"));
        assertAll("Rating check",
                () -> assertArrayEquals(new List[]{ratingList}, new List[]{ratingStorage.getRatings()}, "Wrong rating list"),
                () -> assertEquals(new Rating(1, "G"), ratingStorage.getRatingById(1), "Wrong rating by id")
        );
    }
}