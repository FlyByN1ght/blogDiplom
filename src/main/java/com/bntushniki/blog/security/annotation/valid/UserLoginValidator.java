package com.bntushniki.blog.security.annotation.valid;

import com.bntushniki.blog.security.annotation.ExactValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserLoginValidator implements ConstraintValidator<ExactValue, Integer> {

    @Override
    public void initialize(ExactValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value == 10;
    }
}