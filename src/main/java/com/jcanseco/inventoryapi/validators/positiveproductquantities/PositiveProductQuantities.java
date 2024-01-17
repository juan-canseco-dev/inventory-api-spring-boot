package com.jcanseco.inventoryapi.validators.positiveproductquantities;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PositiveProductQuantitiesValidator.class)
public @interface PositiveProductQuantities {
    String message() default "The Products Quantity must be greater than zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
