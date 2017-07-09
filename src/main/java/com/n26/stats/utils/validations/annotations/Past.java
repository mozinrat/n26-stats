package com.n26.stats.utils.validations.annotations;

/**
 * Created by rverma on 7/8/17.
 */

import com.n26.stats.utils.validations.PastConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(
        validatedBy = PastConstraintValidator.class
)
public @interface Past {
    String message() default "{com.n26.stats.utils.validations.annotations.Past.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Past[] value();
    }
}

