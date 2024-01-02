package com.jcanseco.inventoryapi.validation.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateCustomerDtoValidationTests {
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
    public void updateCustomerDtoWhenAllPropertiesValidValidationShouldNotFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void updateCustomerDtoWhenCustomerIdIsNotPresentValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }


    @Test
    public void updateCustomerDtoWhenCustomerIdIsLessThanOneValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(0L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenDniIsEmptyValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenDniIsNotPresentValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenDniSizeExceedsMaxValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6Z")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenDniIsBlankValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
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
    public void updateCustomerDtoWhenPhoneIsEmptyValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenPhoneIsNotPresentValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCustomerDtoPhoneSizeExceedsMaxValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-5678-789-456-123")
                .fullName("John Doe")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenPhoneIsBlankValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone(" ")
                .fullName("John Doe")
                .address(buildAddress() )
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenFullNameIsEmptyValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenFullNameIsNotPresentValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    public void updateCustomerDtoFullNameSizeExceedsMaxValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("Alexander Maximilian Bartholomew Jefferson Fitzgerald III")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenFullNameIsBlankValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName(" ")
                .address(buildAddress())
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenAddressIsNotPresentValidationShouldFail() {
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }

    @Test
    public void updateCustomerDtoWhenAddressIsInvalidValidationShouldFail() {
        var address = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .build();
        var dto = UpdateCustomerDto.builder()
                .customerId(1L)
                .dni("uR7gF9L3jP2qY5sH8wX6")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(address)
                .build();
        var violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}
