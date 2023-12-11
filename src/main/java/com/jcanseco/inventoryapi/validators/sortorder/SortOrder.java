package com.jcanseco.inventoryapi.validators.sortorder;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = SortOrderValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SortOrder {
    String message() default "Invalid Sort Order. The following options are valid: 'asc', 'ascending', 'desc', 'descending'.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
