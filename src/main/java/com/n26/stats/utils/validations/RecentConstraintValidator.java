package com.n26.stats.utils.validations;

import com.n26.stats.utils.validations.annotations.Recent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.ZonedDateTime;

public class RecentConstraintValidator implements ConstraintValidator<Recent, ZonedDateTime> {

    private long timeFromNowInSeconds;

    @Override
    public void initialize(Recent recent) {
        this.timeFromNowInSeconds = recent.timeFromNowInSeconds();
    }

    @Override
    public boolean isValid(ZonedDateTime object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return false;
        }
        return Duration.between(object,ZonedDateTime.now(object.getZone())).getSeconds() < timeFromNowInSeconds;
    }
}