package com.bntushniki.blog.security.annotation.valid;

import com.bntushniki.blog.security.annotation.ExactValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for validating user login.
 * @author Daniil
 */
public class UserLoginValidator implements ConstraintValidator<ExactValue, String> {

    @Override
    public void initialize(ExactValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the user login.
     *
     * @param value   the user login to be validated
     * @param context the constraint validator context
     * @return true if the user login is valid, otherwise false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("\\d{10}");
    }
}
