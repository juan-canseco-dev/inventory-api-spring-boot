package com.jcanseco.inventoryapi.validation.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreateCustomerDtoValidationTests {

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
    public void createCustomerDtoWhenAllPropertiesValidValidationShouldNotFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void createCustomerDtoWhenDniIsEmptyValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createCustomerDtoWhenDniIsNotPresentValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createCustomerDtoWhenDniSizeExceedsMaxValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6Z")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createCustomerDtoWhenDniIsBlankValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni(" ")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress() )
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
    // Phone
    @Test
    public void createCustomerDtoWhenPhoneIsEmptyValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createCustomerDtoWhenPhoneIsNotPresentValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createCustomerDtoPhoneSizeExceedsMaxValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-5678-789-456-123")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createCustomerDtoWhenPhoneIsBlankValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone(" ")
                .fullName("John Doe")
                .address(buildAddress() )
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createCustomerDtoWhenFullNameIsEmptyValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createCustomerDtoWhenFullNameIsNotPresentValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void createCustomerDtoFullNameSizeExceedsMaxValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("Alexander Maximilian Bartholomew Jefferson Fitzgerald III")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createCustomerDtoWhenFullNameIsBlankValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName(" ")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createCustomerDtoWhenAddressIsNotPresentValidationShouldFail() {
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void createCustomerDtoWhenAddressIsInvalidValidationShouldFail() {
        var address = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .build();
        var dto = CreateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(address)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}
