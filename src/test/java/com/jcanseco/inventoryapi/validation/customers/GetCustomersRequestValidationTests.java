package com.jcanseco.inventoryapi.validation.customers;

import com.jcanseco.inventoryapi.dtos.customers.GetCustomersRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class GetCustomersRequestValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getCustomersRequestWhenFieldsAreNotPresentValidationShouldNotFail() {
        var request = GetCustomersRequest.builder().build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getCustomersRequestWhenAllFieldsArePresentValidationShouldNotFail() {
        var request = GetCustomersRequest.builder()
                .dni("1")
                .fullName("a")
                .phone("c")
                .pageNumber(1)
                .pageSize(1)
                .orderBy("fullName")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getCustomersRequestWhenPageNumberIsLessThanOneValidationShouldFail() {
        var request = GetCustomersRequest.builder()
                .pageNumber(0)
                .pageSize(1)
                .orderBy("fullName")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void getCustomersRequestWhenPageSizeIsLessThanOneValidationShouldFail() {
        var request = GetCustomersRequest.builder()
                .pageNumber(1)
                .pageSize(0)
                .orderBy("fullName")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }


    @ParameterizedTest
    @ValueSource(strings = {"id", "dni", "fullName", "phone"})
    public void getCustomersRequestWhenOrderByIsValidValidationShouldNotFail(String orderBy) {
        var request = GetCustomersRequest.builder()
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
    public void getCustomersRequestWhenOrderByIsInvalidValidValidationShouldFail(String orderBy) {
        var request = GetCustomersRequest.builder()
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
    public void getCustomersRequestWhenSortOrderIsValidValidationShouldNotFail(String sortOrder) {
        var request = GetCustomersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .orderBy("fullName")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"asce", "ascendingf", "desce", "descendingf"})
    public void getCustomersRequestWhenSortOrderIsInvalidValidationShouldFail(String sortOrder) {
        var request = GetCustomersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .orderBy("fullName")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

}
