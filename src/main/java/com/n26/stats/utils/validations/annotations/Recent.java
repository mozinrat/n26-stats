package com.n26.stats.utils.validations.annotations;

/**
 * Created by rverma on 7/8/17.
 */

import com.n26.stats.utils.validations.RecentConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = RecentConstraintValidator.class
)
public @interface Recent {
    long timeFromNowInSeconds() default Long.MAX_VALUE;

    String message() default "{com.n26.stats.utils.validations.annotations.Recent.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Recent[] value();
    }
}
