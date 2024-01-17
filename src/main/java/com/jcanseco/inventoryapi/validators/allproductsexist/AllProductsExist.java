package com.jcanseco.inventoryapi.validators.allproductsexist;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AllProductsExistValidator.class)
public @interface AllProductsExist {
    String message() default "All Products must exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
