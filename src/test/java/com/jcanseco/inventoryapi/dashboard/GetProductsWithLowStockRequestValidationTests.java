package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.dto.GetProductsWithLowStockRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetProductsWithLowStockRequestValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getProductsWithLowStockRequestWhenFieldsAreValidValidationShouldNotFail() {
        var request = GetProductsWithLowStockRequest.builder()
                .stockThreshold(5L)
                .limit(2)
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getProductsWithLowStockRequestWhenThresholdIsNegativeValidationShouldFail() {
        var request = GetProductsWithLowStockRequest.builder()
                .stockThreshold(-1L)
                .limit(2)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void getProductsWithLowStockRequestWhenLimitIsLessThanOneValidationShouldFail() {
        var request = GetProductsWithLowStockRequest.builder()
                .stockThreshold(5L)
                .limit(0)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}
