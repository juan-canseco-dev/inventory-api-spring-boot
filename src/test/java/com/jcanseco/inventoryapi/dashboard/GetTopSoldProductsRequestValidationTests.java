package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.dto.GetTopSoldProductsRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetTopSoldProductsRequestValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getTopSoldProductsRequestWhenLimitIsValidValidationShouldNotFail() {
        var request = new GetTopSoldProductsRequest(2);
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getTopSoldProductsRequestWhenLimitIsLessThanOneValidationShouldFail() {
        var request = new GetTopSoldProductsRequest(0);
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}
