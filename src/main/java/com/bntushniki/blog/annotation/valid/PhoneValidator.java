package com.bntushniki.blog.annotation.valid;

import com.bntushniki.blog.annotation.ValidPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * A validator class to validate phone numbers based on a specific pattern.
 */
@Component
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            return false;
        }
        return Pattern.matches(PHONE_PATTERN, phoneField);
    }

    /**
     * Checks if a phone number is valid.
     * @param phone The phone number to be checked.
     * @return True if the phone number is valid, false otherwise.
     */
    public Boolean phoneValid(String phone) {
        return Pattern.matches(PHONE_PATTERN, phone);
    }
}
