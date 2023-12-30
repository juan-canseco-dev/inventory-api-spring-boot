package com.jcanseco.inventoryapi.validation.units;

import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class GetUnitsOfMeasurementRequestValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getUnitsOfMeasurementRequestWhenFieldsAreNotPresentValidationShouldNotFail() {
        var request = GetUnitsOfMeasurementRequest.builder().build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getUnitsOfMeasurementRequestWhenAllFieldsArePresentValidationShouldNotFail() {
        var request = GetUnitsOfMeasurementRequest.builder()
                .name("a")
                .pageNumber(1)
                .pageSize(1)
                .orderBy("name")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getUnitsOfMeasurementRequestWhenPageNumberIsLessThanOneValidationShouldFail() {
        var request = GetUnitsOfMeasurementRequest.builder()
                .name("a")
                .pageNumber(0)
                .pageSize(1)
                .orderBy("name")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void getUnitsOfMeasurementRequestWhenPageSizeIsLessThanOneValidationShouldFail() {
        var request = GetUnitsOfMeasurementRequest.builder()
                .name("a")
                .pageNumber(1)
                .pageSize(0)
                .orderBy("name")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }


    @ParameterizedTest
    @ValueSource(strings = {"id", "name"})
    public void getUnitsOfMeasurementRequestWhenOrderByIsValidValidationShouldNotFail(String orderBy) {
        var request = GetUnitsOfMeasurementRequest.builder()
                .name("a")
                .pageNumber(1)
                .pageSize(1)
                .orderBy(orderBy)
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid_field"})
    public void getUnitsOfMeasurementRequestWhenOrderByIsInvalidValidValidationShouldFail(String orderBy) {
        var request = GetUnitsOfMeasurementRequest.builder()
                .name("a")
                .pageNumber(1)
                .pageSize(1)
                .orderBy(orderBy)
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"asc", "ascending", "desc", "descending"})
    public void getUnitsOfMeasurementRequestWhenSortOrderIsValidValidationShouldNotFail(String sortOrder) {
        var request = GetUnitsOfMeasurementRequest.builder()
                .name("a")
                .pageNumber(1)
                .pageSize(1)
                .orderBy("name")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"asce", "ascendingf", "desce", "descendingf"})
    public void getUnitsOfMeasurementRequestWhenSortOrderIsInvalidValidationShouldFail(String sortOrder) {
        var request = GetUnitsOfMeasurementRequest.builder()
                .name("a")
                .pageNumber(1)
                .pageSize(1)
                .orderBy("name")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
