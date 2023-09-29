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
    private final String name;

    public Genre(int id) {
        this.id = id;
        switch (id) {
            case 1:
                this.name = "Комедия";
                return;
            case 2:
                this.name = "Драма";
                return;
            case 3:
                this.name = "Мультфильм";
                return;
            case 4:
                this.name = "Триллер";
                return;
            case 5:
                this.name = "Документальный";
                return;
            case 6:
                this.name = "Боевик";
                return;
            default:
                this.name = null;
                break;
        }
    }

    public Genre(int id, String name) {
        this.id = id;
        switch (id) {
            case 1:
                this.name = "COMEDY";
                return;
            case 2:
                this.name = "DRAMA";
                return;
            case 3:
                this.name = "CARTOON";
                return;
            case 4:
                this.name = "THRILLER";
                return;
            case 5:
                this.name = "DOCUMENTARY";
                return;
            case 6:
                this.name = "ACTION";
                return;
            default:
                this.name = null;
                break;
        }
    }
}

