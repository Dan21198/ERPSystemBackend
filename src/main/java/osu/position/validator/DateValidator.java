package osu.position.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<DateConstraint, Object> {

    @Override
    public void initialize(DateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Field startDateField = object.getClass().getDeclaredField("startDate");
            Field endDateField = object.getClass().getDeclaredField("endDate");

            startDateField.setAccessible(true);
            endDateField.setAccessible(true);

            LocalDate startDate = (LocalDate) startDateField.get(object);
            LocalDate endDate = (LocalDate) endDateField.get(object);

            if (startDate == null || endDate == null) {
                return true;
            }

            if (endDate.isBefore(startDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("End date must be after or equal to start date")
                        .addPropertyNode("endDate")
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to validate date range", e);
        }
    }
}