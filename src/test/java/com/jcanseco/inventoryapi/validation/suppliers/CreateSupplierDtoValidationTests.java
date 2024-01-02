package com.jcanseco.inventoryapi.validation.suppliers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreateSupplierDtoValidationTests {
    private Validator validator;

    private AddressDto buildAddress() {
        return AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createSupplierWhenAllPropertiesArePresentValidationShouldNotFail() {

        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void createSupplierWhenCompanyNameIsEmptyValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createSupplierWhenCompanyNameIsNotPresentValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createSupplierWhenCompanyNameSizeIsExceededValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("Tech Innovations International Solutions Group Inc.")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createSupplierWhenCompanyNameIsBlankValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName(" ")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createSupplierWhenContactNameIsEmptyValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var validations = validator.validate(dto);
        assertEquals(2, validations.size());
    }

    @Test
    public void createSupplierWhenContactNameIsNotPresentValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }


    @Test
    public void createSupplierWhenContactNameSizeIsExceededValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Alexander Maximilian Bartholomew Jefferson Fitzgerald III")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createSupplierWhenContactNameIsBlankValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName(" ")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createSupplierWhenContactPhoneIsEmptyValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createSupplierWhenContactPhoneIsNotPresentValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createSupplierWhenContactPhoneSizeIsExceededValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-5678-789-456-123")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createSupplierWhenContactPhoneIsBlankValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone(" ")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createSupplierWhenAddressIsNotPresentValidationShouldFail() {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createSupplierWhenAddressIsInvalidValidationShouldFail() {
        var address = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .build();
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}
