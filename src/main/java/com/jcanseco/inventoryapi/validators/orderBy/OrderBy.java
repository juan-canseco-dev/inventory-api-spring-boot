package com.jcanseco.inventoryapi.validators.orderBy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = OrderByValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderBy {
    String[] fields();
    String message() default "Invalid Order By Field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
