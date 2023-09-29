package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Getter
@Setter
@EqualsAndHashCode
public class Rating {
    @Max(5)
    @Positive
    private final int id;
    private final String name;


    public Rating(int id) {
        this.id = id;
        switch (id) {
            case 1:
                this.name = "G";
                return;
            case 2:
                this.name = "PG";
                return;
            case 3:
                this.name = "PG-13";
                return;
            case 4:
                this.name = "R";
                return;
            case 5:
                this.name = "NC-17";
                return;
            default:
                this.name = null;
                break;
        }
    }

    public Rating(int id, String name) {
        this.id = id;
        switch (id) {
            case 1:
                this.name = "G";
                return;
            case 2:
                this.name = "PG";
                return;
            case 3:
                this.name = "PG-13";
                return;
            case 4:
                this.name = "R";
                return;
            case 5:
                this.name = "NC-17";
                return;
            default:
                this.name = null;
                break;
        }
    }
}