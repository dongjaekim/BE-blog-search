package com.blogsearch.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface SortValid {

    String message() default "invalid sort value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    SortParam source() default SortParam.KAKAO;
}
