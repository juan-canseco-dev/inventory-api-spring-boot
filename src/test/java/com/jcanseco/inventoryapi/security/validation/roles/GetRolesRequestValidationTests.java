package com.jcanseco.inventoryapi.security.validation.roles;


import com.jcanseco.inventoryapi.security.dtos.roles.GetRolesRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class GetRolesRequestValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getRolesRequestWhenFieldsAreNotPresentValidationShouldNotFail() {
        var request = GetRolesRequest.builder().build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getRolesRequestWhenAllFieldsArePresentValidationShouldNotFail() {
        var request = GetRolesRequest.builder()
                .name("p")
                .pageNumber(1)
                .pageSize(1)
                .orderBy("name")
                .sortOrder("asc")
                .build();
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void getRolesRequestWhenPageNumberIsLessThanOneValidationShouldFail() {
        var request = GetRolesRequest.builder()
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
    public void getRolesRequestWhenPageSizeIsLessThanOneValidationShouldFail() {
        var request = GetRolesRequest.builder()
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
    @ValueSource(strings = {"id", "name", "createdAt", "updatedAt"})
    public void getRolesRequestWhenOrderByIsValidValidationShouldNotFail(String orderBy) {
        var request = GetRolesRequest.builder()
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
    public void getRolesRequestWhenOrderByIsInvalidValidValidationShouldFail(String orderBy) {
        var request = GetRolesRequest.builder()
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
    public void getRolesRequestWhenSortOrderIsValidValidationShouldNotFail(String sortOrder) {
        var request = GetRolesRequest.builder()
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
    public void getRolesRequestWhenSortOrderIsInvalidValidationShouldFail(String sortOrder) {
        var request = GetRolesRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .orderBy("name")
                .sortOrder(sortOrder)
                .build();
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
