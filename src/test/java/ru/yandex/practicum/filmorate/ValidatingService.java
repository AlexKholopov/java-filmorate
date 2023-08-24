package ru.yandex.practicum.filmorate;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
class ValidatingService {

    private final Validator validator;

    ValidatingService(Validator validator) {
        this.validator = validator;
    }

    void validateInputWithInjectedValidator(Object o) {
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
    /*
    сделал через object, наверное не очень хороший вариант... еще почитаю поподробнее,
    разберусь как я вообще сделал кастомную валидацию и попробую посмотреть что там вообще внутри происходит
     */
}