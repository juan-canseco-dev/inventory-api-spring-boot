package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.dto.GetTopSuppliersByRevenueRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetTopSuppliersByRevenueRequestValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getTopSuppliersByRevenueRequestWhenLimitIsValidValidationShouldNotFail() {
        var request = GetTopSuppliersByRevenueRequest.builder()
                .limit(2)
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getTopSuppliersByRevenueRequestWhenLimitIsLessThanOneValidationShouldFail() {
        var request = GetTopSuppliersByRevenueRequest.builder()
                .limit(0)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}
