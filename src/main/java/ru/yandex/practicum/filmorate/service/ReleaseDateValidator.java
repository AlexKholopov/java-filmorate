package ru.yandex.practicum.filmorate.service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return localDate.isAfter(LocalDate.of(1895, 12, 28));
        } catch (Exception e) {
            return false;
        }
    }
}
