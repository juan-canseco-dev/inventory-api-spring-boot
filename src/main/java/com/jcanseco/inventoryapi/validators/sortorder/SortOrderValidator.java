package com.jcanseco.inventoryapi.validators.sortorder;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class SortOrderValidator implements ConstraintValidator<SortOrder, String> {
    private List<String> sortOrders;

    @Override
    public void initialize(SortOrder constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.sortOrders = List.of("asc", "ascending", "desc", "descending");
    }

    @Override
    public boolean isValid(String sortOrder, ConstraintValidatorContext constraintValidatorContext) {
        if (sortOrder == null)
            return  true;
        if (sortOrder.isEmpty())
            return true;
        return sortOrders.contains(sortOrder);
    }
}
