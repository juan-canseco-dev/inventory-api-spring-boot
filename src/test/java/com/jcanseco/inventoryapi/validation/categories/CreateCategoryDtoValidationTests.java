package com.jcanseco.inventoryapi.validation.categories;

import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreateCategoryDtoValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createCategoryDtoWhenNameIsPresentValidationShouldNotFail() {
        var dto = CreateCategoryDto.builder()
                .name("New Category")
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void createCategoryDtoWhenNameIsEmptyValidationShouldFail() {
        var dto = CreateCategoryDto.builder()
                .name("")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void createCategoryDtoWhenNameIsNotPresentValidationShouldFail() {
        var dto = CreateCategoryDto.builder()
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void createCategoryWhenNameSizeExceedsMaxValidationShouldFail() {
        var dto = CreateCategoryDto.builder()
                .name("EclecticCulinaryDelightsFromAroundTheWorldAndBeyond")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void createCategoryWhenNameIsBlankValidationShouldFail() {
        var dto = CreateCategoryDto.builder()
                .name(" ")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}
