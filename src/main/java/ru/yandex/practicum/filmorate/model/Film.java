package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.service.ReleaseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@Builder
public class Film {
    int id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @ReleaseDate
    LocalDate releaseDate;
    @Min(0)
    int duration;
}
