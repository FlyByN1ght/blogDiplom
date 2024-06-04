package com.bntushniki.blog.security.annotation.valid;

import com.bntushniki.blog.security.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Validator for validating passwords.
 * @author Daniil
 */
@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    /**
     * Validates the password.
     *
     * @param password the password to be validated
     * @param context  the constraint validator context
     * @return true if the password is valid, otherwise false
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && Pattern.matches(PASSWORD_PATTERN, password);
    }

    /**
     * Checks if a password is valid.
     *
     * @param phone the password to be checked
     * @return true if the password is valid, otherwise false
     */
    public Boolean passwordValid(String phone) {
        return Pattern.matches(PASSWORD_PATTERN, phone);
    }
}
