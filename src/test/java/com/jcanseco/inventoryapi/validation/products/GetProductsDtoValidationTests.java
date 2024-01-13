package com.jcanseco.inventoryapi.validation.products;

import com.jcanseco.inventoryapi.dtos.products.GetProductsRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class GetProductsDtoValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getProductsWhenFieldsAreNotPresentValidationShouldNotFail() {
        var request = GetProductsRequest.builder().build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getProductsWhenSupplierIdIsLessThanOneValidationShouldFail() {
        var request = GetProductsRequest.builder()
                .supplierId(0L)
                .build();
        var violations = validator.validate(request);
        assertEquals(1, violations.size());
    }


    @Test
    public void getProductsWhenCategoryIdIsLessThanOneValidationShouldFail() {
        var request = GetProductsRequest.builder()
                .categoryId(0L)
                .build();
        var violations = validator.validate(request);
        assertEquals(1, violations.size());
    }


    @Test
    public void getProductsWhenUnitIdIsLessThanOneValidationShouldFail() {
        var request = GetProductsRequest.builder()
                .unitId(0L)
                .build();
        var violations = validator.validate(request);
        assertEquals(1, violations.size());
    }

    @Test
    public void getProductsWhenAllFieldsArePresentValidationShouldNotFail() {
        var request = GetProductsRequest.builder()
                .supplierId(1L)
                .categoryId(2L)
                .unitId(1L)
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
    public void getProductsWhenPageNumberIsLessThanOneValidationShouldFail() {
        var request = GetProductsRequest.builder()
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
    public void getProductsWhenPageSizeIsLessThanOneValidationShouldFail() {
        var request = GetProductsRequest.builder()
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
    @ValueSource(strings = {"id", "name", "stock", "supplier", "category", "unit"})
    public void getProductsWhenOrderByIsValidValidationShouldNotFail(String orderBy) {
        var request = GetProductsRequest.builder()
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
    public void getProductsWhenOrderByIsInvalidValidValidationShouldFail(String orderBy) {
        var request = GetProductsRequest.builder()
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
    public void getProductsWhenSortOrderIsValidValidationShouldNotFail(String sortOrder) {
        var request = GetProductsRequest.builder()
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
    public void getProductsWhenSortOrderIsInvalidValidationShouldFail(String sortOrder) {
        var request = GetProductsRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .orderBy("name")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
