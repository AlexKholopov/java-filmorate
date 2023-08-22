package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
}
