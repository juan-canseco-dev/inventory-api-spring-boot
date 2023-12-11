package com.jcanseco.inventoryapi.validators.orderBy;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class OrderByValidator implements ConstraintValidator<OrderBy, String> {
    private String[] fields;

    @Override
    public void initialize(OrderBy constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(String orderBy, ConstraintValidatorContext constraintValidatorContext) {
        if (orderBy == null)
            return true;
        if (orderBy.isEmpty())
            return true;
        return Arrays.asList(fields).contains(orderBy);
    }
}
