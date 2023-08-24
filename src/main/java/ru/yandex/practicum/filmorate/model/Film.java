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
    @NotBlank(message = "must not be blank")
    String name;
    @Size(message = "size must be between 0 and 200",
            max = 200)
    String description;
    @ReleaseDate
    LocalDate releaseDate;
    @Min(value = 0, message = "must be greater than or equal to 0")
    int duration;
}
