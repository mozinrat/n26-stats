package com.n26.stats.utils.validations;

import com.n26.stats.utils.validations.annotations.Past;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZonedDateTime;

/**
 * Created by payal on 7/9/17.
 */
public class PastConstraintValidator implements ConstraintValidator<Past, ZonedDateTime> {

    @Override
    public void initialize(Past past) {
    }

    @Override
    public boolean isValid(ZonedDateTime object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return false;
        }
        return object.compareTo(ZonedDateTime.now(object.getZone())) <= 0;
    }
}
