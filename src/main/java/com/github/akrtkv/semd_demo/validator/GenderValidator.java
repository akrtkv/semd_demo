package com.github.akrtkv.semd_demo.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class GenderValidator implements ConstraintValidator<Gender, String> {

    private final MessageSource messageSource;

    @Autowired
    public GenderValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!value.equals("М") && !value.equals("Ж")) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    messageSource.getMessage("genderNotValidMessage", null, Locale.getDefault())
            ).addPropertyNode("Patient.gender").addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }
}
