package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@EqualsAndHashCode
@Getter
public class Genre {
    @Max(6)
    @Positive
    private final int id;
    private final GenreName name;


    public Genre(int id, String name) {
        this.id = id;
        if (name == null) {
            this.name = null;
        } else {
            this.name = GenreName.valueOf(name);
        }
    }

    public String getName() {
        if (name == null) {
            return null;
        } else {
            return name.filmName;
        }
    }


    private enum GenreName {
        COMEDY("Комедия"),
        THRILLER("Триллер"),
        ACTION("Боевик"),
        DRAMA("Драма"),
        CARTOON("Мультфильм"),
        DOCUMENTARY("Документальный");

        private final String filmName;

        GenreName(String filmName) {
            this.filmName = filmName;
        }

        String getFilmName() {
            return this.filmName;
        }
    }
}

