package com.jcanseco.inventoryapi.catalog.products.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;






@Documented
@Constraint(validatedBy = ProductPricesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductPrices {

    String message() default "Sale price must be greater than or equal to purchase price";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}







