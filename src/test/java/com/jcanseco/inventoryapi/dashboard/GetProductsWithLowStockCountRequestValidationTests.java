package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.dto.GetProductsWithLowStockCountRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetProductsWithLowStockCountRequestValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getProductsWithLowStockCountRequestWhenThresholdIsNotPresentValidationShouldNotFail() {
        var request = GetProductsWithLowStockCountRequest.builder().build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getProductsWithLowStockCountRequestWhenThresholdIsValidValidationShouldNotFail() {
        var request = GetProductsWithLowStockCountRequest.builder()
                .stockThreshold(0L)
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getProductsWithLowStockCountRequestWhenThresholdIsNegativeValidationShouldFail() {
        var request = GetProductsWithLowStockCountRequest.builder()
                .stockThreshold(-1L)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
