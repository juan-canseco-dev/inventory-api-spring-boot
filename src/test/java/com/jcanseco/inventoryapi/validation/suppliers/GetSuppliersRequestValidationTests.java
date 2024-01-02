package com.jcanseco.inventoryapi.validation.suppliers;

import com.jcanseco.inventoryapi.dtos.suppliers.GetSuppliersRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;


public class GetSuppliersRequestValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getSuppliersWhenFieldsAreNotPresentValidationShouldNotFail() {
        var request = GetSuppliersRequest.builder().build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getSuppliersWhenAllFieldsArePresentValidationShouldNotFail() {
        var request = GetSuppliersRequest.builder()
                .companyName("a")
                .contactName("s")
                .contactPhone("555")
                .pageNumber(1)
                .pageSize(1)
                .orderBy("companyName")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getSuppliersWhenPageNumberIsLessThanOneValidationShouldFail() {
        var request = GetSuppliersRequest.builder()
                .pageNumber(0)
                .pageSize(1)
                .orderBy("companyName")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void getSuppliersWhenPageSizeIsLessThanOneValidationShouldFail() {
        var request = GetSuppliersRequest.builder()
                .pageNumber(1)
                .pageSize(0)
                .orderBy("companyName")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }


    @ParameterizedTest
    @ValueSource(strings = {"id", "companyName", "contactName", "contactPhone"})
    public void getSuppliersWhenOrderByIsValidValidationShouldNotFail(String orderBy) {
        var request = GetSuppliersRequest.builder()
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
    public void getSuppliersWhenOrderByIsInvalidValidValidationShouldFail(String orderBy) {
        var request = GetSuppliersRequest.builder()
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
    public void getSuppliersWhenSortOrderIsValidValidationShouldNotFail(String sortOrder) {
        var request = GetSuppliersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .orderBy("companyName")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"asce", "ascendingf", "desce", "descendingf"})
    public void getSuppliersWhenSortOrderIsInvalidValidationShouldFail(String sortOrder) {
        var request = GetSuppliersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .orderBy("companyName")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
