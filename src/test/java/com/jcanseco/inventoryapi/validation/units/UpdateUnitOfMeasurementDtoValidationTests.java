package com.jcanseco.inventoryapi.validation.units;

import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUnitOfMeasurementDtoValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void updateUnitOfMeasurementDtoDtoWhenFieldsArePresentValidationShouldNotFail() {
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(1L)
                .name("Updated Name")
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void updateUnitOfMeasurementDtoWhenNameIsEmptyValidationShouldFail() {
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(1L)
                .name("")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void updateUnitOfMeasurementDtoWhenNameIsNotPresentValidationShouldFail() {
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(1L)
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    public void updateUnitOfMeasurementDtoWhenNameSizeExceedsMaxValidationShouldFail() {
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(1L)
                .name("ElectromagneticWavelengthOfUltraVioletLightInNanometers")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void updateUnitOfMeasurementWhenNameIsBlankValidationShouldFail() {
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(1L)
                .name(" ")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void updateUnitOfMeasurementWhenUnitOfMeasurementIdIsNotPresentValidationShouldFail() {
        var dto = UpdateUnitOfMeasurementDto.builder()
                .name("Updated Unit")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    public void updateUnitOfMeasurementWhenUnitOfMeasurementIdIsLessThanOneValidationShouldFail() {
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(0L)
                .name("Updated Unit")
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}
