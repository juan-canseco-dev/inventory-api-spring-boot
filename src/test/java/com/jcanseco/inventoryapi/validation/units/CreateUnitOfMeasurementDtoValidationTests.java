package com.jcanseco.inventoryapi.validation.units;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreateUnitOfMeasurementDtoValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createUnitOfMeasurementDtoWhenNameIsPresentValidationShouldNotFail() {
        var dto = CreateUnitOfMeasurementDto.builder()
                .name("New Unit")
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void createUnitOfMeasurementDtoWhenNameIsEmptyValidationShouldFail() {
        var dto = CreateUnitOfMeasurementDto.builder()
                .name("")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void createUnitOfMeasurementDtoWhenNameIsNotPresentValidationShouldFail() {
        var dto = CreateUnitOfMeasurementDto.builder()
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void createUnitOfMeasurementWhenNameSizeExceedsMaxValidationShouldFail() {
        var dto = CreateUnitOfMeasurementDto.builder()
                .name("ElectromagneticWavelengthOfUltraVioletLightInNanometers")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void createUnitOfMeasurementWhenNameIsBlankValidationShouldFail() {
        var dto = CreateUnitOfMeasurementDto.builder()
                .name(" ")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

}
