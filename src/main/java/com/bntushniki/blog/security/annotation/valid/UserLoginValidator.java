package com.bntushniki.blog.security.annotation.valid;

import com.bntushniki.blog.security.annotation.ExactValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserLoginValidator implements ConstraintValidator<ExactValue, String> {

    @Override
    public void initialize(ExactValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("\\d{10}");
    }
}