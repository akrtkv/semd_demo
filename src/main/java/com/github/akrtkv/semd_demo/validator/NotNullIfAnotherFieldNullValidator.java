package com.github.akrtkv.semd_demo.validator;

import com.github.akrtkv.semd_demo.exception.ValidatorException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

@Component
public class NotNullIfAnotherFieldNullValidator implements ConstraintValidator<NotNullIfAnotherFieldNull, Object> {

    private final MessageSource messageSource;

    private String fieldName;

    private String dependFieldName;

    private String message;

    @Autowired
    public NotNullIfAnotherFieldNullValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(NotNullIfAnotherFieldNull constraintAnnotation) {
        fieldName = constraintAnnotation.fieldName();
        dependFieldName = constraintAnnotation.dependFieldName();
        if (fieldName.equals("drug")) {
            message = messageSource.getMessage("drugNotNullIfDrugsNullValidationMessage", null, Locale.getDefault());
        } else {
            message = messageSource.getMessage("drugsNotNullIfDrugNullValidationMessage", null, Locale.getDefault());
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            var fieldValue = BeanUtils.getProperty(value, fieldName);
            var dependFieldValue = BeanUtils.getProperty(value, dependFieldName);
            if (fieldValue == null && dependFieldValue == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ValidatorException(e);
        }
        return true;
    }
}

