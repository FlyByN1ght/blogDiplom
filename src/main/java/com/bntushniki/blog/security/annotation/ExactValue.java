package com.bntushniki.blog.security.annotation;

import com.bntushniki.blog.security.annotation.valid.UserLoginValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {UserLoginValidator.class})
public @interface ExactValue {
    String message() default "Must contain exactly 10 digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}