package ru.yandex.practicum.filmorate.service;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseDate {

    String message() default "Validation exception: The release date must not be earlier than December 28, 1895";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };


}
