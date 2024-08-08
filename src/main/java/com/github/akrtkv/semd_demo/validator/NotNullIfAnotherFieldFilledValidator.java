package su.medsoft.rir.recipe.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import su.medsoft.rir.recipe.exception.ValidatorException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

@Component
public class NotNullIfAnotherFieldFilledValidator implements ConstraintValidator<NotNullIfAnotherFieldFilled, Object> {

    private final MessageSource messageSource;

    private String fieldName;

    private String dependFieldName;

    private String message;

    @Autowired
    public NotNullIfAnotherFieldFilledValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(NotNullIfAnotherFieldFilled constraintAnnotation) {
        fieldName = constraintAnnotation.fieldName();
        dependFieldName = constraintAnnotation.dependFieldName();
        if (fieldName.equals("mkb")) {
            message = messageSource.getMessage("mkbNotNullIfPreferential", null, Locale.getDefault());
        } else if (fieldName.equals("financing")) {
            message = messageSource.getMessage("financingNotNullIfPreferential", null, Locale.getDefault());
        } else {
            message = messageSource.getMessage("benefitNotNullIfPreferential", null, Locale.getDefault());
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
            if (fieldValue == null && dependFieldValue != null) {
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

