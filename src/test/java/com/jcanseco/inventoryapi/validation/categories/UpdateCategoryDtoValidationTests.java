package com.jcanseco.inventoryapi.validation.categories;

import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateCategoryDtoValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void updateCategoryDtoWhenFieldsArePresentValidationShouldNotFail() {
        var dto = UpdateCategoryDto.builder()
                .categoryId(1L)
                .name("Updated Name")
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void updateCategoryDtoWhenNameIsEmptyValidationShouldFail() {
        var dto = UpdateCategoryDto.builder()
                .categoryId(1L)
                .name("")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCategoryDtoWhenNameIsNotPresentValidationShouldFail() {
        var dto = UpdateCategoryDto.builder()
                .categoryId(1L)
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCategoryWhenNameSizeExceedsMaxValidationShouldFail() {
        var dto = UpdateCategoryDto.builder()
                .categoryId(1L)
                .name("EclecticCulinaryDelightsFromAroundTheWorldAndBeyond")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCategoryWhenNameIsBlankValidationShouldFail() {
        var dto = UpdateCategoryDto.builder()
                .categoryId(1L)
                .name(" ")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCategoryWhenCategoryIdIsNotPresentValidationShouldFail() {
        var dto = UpdateCategoryDto.builder()
                .name("Updated Category")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCategoryWhenCategoryIdIsLessThanOneValidationShouldFail() {
        var dto = UpdateCategoryDto.builder()
                .categoryId(0L)
                .name("Updated Category")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

}
