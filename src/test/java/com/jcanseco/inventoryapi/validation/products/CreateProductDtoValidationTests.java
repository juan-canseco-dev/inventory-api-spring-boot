package com.jcanseco.inventoryapi.validation.products;

import com.jcanseco.inventoryapi.dtos.products.CreateProductDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreateProductDtoValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createProductWhenAllPropertiesArePresentValidationShouldNotFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void createProductWhenSupplierIdIsNotPresentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .categoryId(1L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenSupplierIdIsLessThanOneValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(0L)
                .categoryId(1L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenCategoryIdIsNotPresentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenCategoryIdIsLessThanOneValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(0L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenUnitIdIsNotPresentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenUnitIdIsLessThanOneValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(0L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }


    @Test
    public void createProductWhenNameIsEmptyValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createProductWhenNameIsNotPresentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createProductWhenNameSizeIsGreaterThan50CharactersValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("TechEleganceUltraGlideWirelessAudioExperienceSystem")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenNameIsBlankValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name(" ")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }


    @Test
    public void createProductWhenPurchasePriceIsNotPresentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Laptop")
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenPurchasePriceIsLessThanOneCentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(0.009)
                .salePrice(59.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenSalePriceIsNotPresentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createProductWhenSalePriceIsLessThanACentValidationShouldFail() {
        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Laptop")
                .purchasePrice(49.99)
                .salePrice(0.009)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}
