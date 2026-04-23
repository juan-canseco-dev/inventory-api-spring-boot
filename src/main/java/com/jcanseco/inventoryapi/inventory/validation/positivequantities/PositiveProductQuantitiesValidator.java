package com.jcanseco.inventoryapi.inventory.validation.positivequantities;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;

public class PositiveProductQuantitiesValidator implements ConstraintValidator<PositiveProductQuantities, Map<Long, Long>> {

    @Override
    public boolean isValid(Map<Long, Long> productQuantities, ConstraintValidatorContext context) {
        if (productQuantities == null) {
            return true;
        }
        return productQuantities.values().stream().allMatch(q -> q > 0L);
    }
}








