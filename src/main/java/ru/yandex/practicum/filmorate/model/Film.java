package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.yandex.practicum.filmorate.service.ReleaseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Value
@EqualsAndHashCode
public class Film {


    long id;
    @NotBlank(message = "must not be blank")
    String name;
    @Size(message = "size must be between 0 and 200",
            max = 200)
    String description;
    @ReleaseDate
    LocalDate releaseDate;
    @Min(value = 0, message = "must be greater than or equal to 0")
    int duration;
    Set<Long> likesId;
    Set<Genre> genres;
    Rating mpa;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Set<Long> likesId,
                Set<Genre> genres, Rating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likesId = Objects.requireNonNullElseGet(likesId, HashSet::new);
        this.genres = Objects.requireNonNullElseGet(genres, HashSet::new);
        this.mpa = mpa;
    }

    public Set<Long> getLikesId() {
        return new HashSet<>(likesId);
    }

}
