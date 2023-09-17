package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
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
@Builder
@EqualsAndHashCode
public class Film {

    public Film(long id, String title, String description, LocalDate releaseDate, int duration, Set<Long> likesId, Genre genre, Rating rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likesId = Objects.requireNonNullElseGet(likesId, HashSet::new);
        this.genre = genre;
        this.rating = rating;
    }

    long id;
    @NotBlank(message = "must not be blank")
    String title;
    @Size(message = "size must be between 0 and 200",
            max = 200)
    String description;
    @ReleaseDate
    LocalDate releaseDate;
    @Min(value = 0, message = "must be greater than or equal to 0")
    int duration;
    Set<Long> likesId;
    Genre genre;
    Rating rating;

    public Set<Long> getLikesId() {
        return new HashSet<>(likesId);
    }
}
