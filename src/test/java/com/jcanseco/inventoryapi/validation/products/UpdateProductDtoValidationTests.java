package com.jcanseco.inventoryapi.validation.products;

import com.jcanseco.inventoryapi.dtos.products.UpdateProductDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UpdateProductDtoValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void updateProductWhenAllPropertiesArePresentValidationShouldNotFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenProductIdIsNotPresentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .supplierId(1L)
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
    public void updateProductWhenProductIdIsLessThanOneValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(-1L)
                .supplierId(1L)
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
    public void updateProductWhenSupplierIdIsNotPresentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenSupplierIdIsLessThanOneValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenCategoryIdIsNotPresentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenCategoryIdIsLessThanOneValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenUnitIdIsNotPresentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenUnitIdIsLessThanOneValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenNameIsEmptyValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenNameIsNotPresentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenNameSizeIsGreaterThan50CharactersValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenNameIsBlankValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenPurchasePriceIsNotPresentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenPurchasePriceIsLessThanOneCentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenSalePriceIsNotPresentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
    public void updateProductWhenSalePriceIsLessThanACentValidationShouldFail() {
        var dto = UpdateProductDto.builder()
                .productId(1L)
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
