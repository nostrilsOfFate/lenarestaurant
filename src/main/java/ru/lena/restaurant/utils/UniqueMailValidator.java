package ru.lena.restaurant.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.lena.restaurant.HasEmail;
import ru.lena.restaurant.model.User;
import ru.lena.restaurant.repository.UserRepository;

@Component
public class UniqueMailValidator  implements Validator {

    @Autowired
    private UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasEmail user = ((HasEmail) target);
        User dbUser = repository.findByEmail(user.getEmail().toLowerCase());
//        if (dbUser != null && !dbUser.getId().equals(user.getId())) {
//            errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
//        }
    }
}
