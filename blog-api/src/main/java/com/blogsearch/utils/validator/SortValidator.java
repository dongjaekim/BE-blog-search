package com.blogsearch.utils.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SortValidator implements ConstraintValidator<SortValid, String> {

    private List<String> sortValue;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValidSortValue = !StringUtils.hasText(value) || sortValue.contains(value);

        if (!isValidSortValue) {
            context.buildConstraintViolationWithTemplate(
                            String.format("invalid sort value, sort value should be any of %s", sortValue)
                    )
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return isValidSortValue;
    }

    @Override
    public void initialize(SortValid constraintAnnotation) {
        SortParam source = constraintAnnotation.source();
        sortValue = List.of(source.getSortAccuracy(), source.getSortRecency());

        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}