package su.medsoft.rir.recipe.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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
