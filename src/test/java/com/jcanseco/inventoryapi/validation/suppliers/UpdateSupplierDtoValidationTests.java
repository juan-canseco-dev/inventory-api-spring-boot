package com.jcanseco.inventoryapi.validation.suppliers;


import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateSupplierDtoValidationTests {
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
    public void updateSupplierWhenAllPropertiesArePresentValidationShouldNotFail() {

        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void updateSupplierWhenSupplierIdIsNotPresentValidationShouldFail() {

        var dto = UpdateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenSupplierIdIsLessThanOneValidationShouldFail() {

        var dto = UpdateSupplierDto.builder()
                .supplierId(-1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenCompanyNameIsEmptyValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateSupplierWhenCompanyNameIsNotPresentValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateSupplierWhenCompanyNameSizeIsExceededValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("Tech Innovations International Solutions Group Inc.")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenCompanyNameIsBlankValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName(" ")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenContactNameIsEmptyValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();

        var validations = validator.validate(dto);
        assertEquals(2, validations.size());
    }

    @Test
    public void updateSupplierWhenContactNameIsNotPresentValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }


    @Test
    public void updateSupplierWhenContactNameSizeIsExceededValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Alexander Maximilian Bartholomew Jefferson Fitzgerald III")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenContactNameIsBlankValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName(" ")
                .contactPhone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenContactPhoneIsEmptyValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateSupplierWhenContactPhoneIsNotPresentValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateSupplierWhenContactPhoneSizeIsExceededValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-5678-789-456-123")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenContactPhoneIsBlankValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone(" ")
                .address(buildAddress())
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenAddressIsNotPresentValidationShouldFail() {
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateSupplierWhenAddressIsInvalidValidationShouldFail() {
        var address = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .build();
        var dto = UpdateSupplierDto.builder()
                .supplierId(1L)
                .companyName("ABC Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}
