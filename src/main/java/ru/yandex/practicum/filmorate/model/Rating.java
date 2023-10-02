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
    private final RATINGS_NAME name;

    public Rating(int id, String name) {
        this.id = id;
        if (name == null) {
            this.name = null;
        } else {
            this.name = RATINGS_NAME.valueOf(name);
        }
    }

    public String getName() {
        if (name == null){
            return null;
        } else {
            return name.filmRating;
        }
    }


    private enum RATINGS_NAME {
        G("G"),
        PG("PG"),
        PG_13("PG-13"),
        R("R"),
        NC_17("NC-17");

        private final String filmRating;

        RATINGS_NAME(String filmRating) {
            this.filmRating = filmRating;
        }

        public String getFilmRating() {
            return this.filmRating;
        }
    }
}