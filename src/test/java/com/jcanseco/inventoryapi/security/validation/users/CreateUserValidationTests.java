package com.jcanseco.inventoryapi.security.validation.users;

import com.jcanseco.inventoryapi.security.dtos.users.CreateUserDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class CreateUserValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createUserWhenPropertiesArePresentValidationShouldNotFail() {
        var dto = CreateUserDto.builder()
                .fullName("Jane Doe")
                .roleId(1L)
                .email("jane.doe@mail.com")
                .password("jane.doe1234")
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidCreateDto")
    public void createUserWhenPropertiesAreInvalidValidationShouldFail(String fullName, Long roleId, String email, String password) {
        var dto = CreateUserDto.builder()
                .fullName(fullName)
                .roleId(roleId)
                .email(email)
                .password(password)
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }


    private static Stream<Arguments> invalidCreateDto() {
        return Stream.of(
                // Invalid Name
                Arguments.arguments(null, 1L, "jane.doe@gmail.com", "password1234"),
                Arguments.arguments("", 1L, "jane.doe@gmail.com", "password1234"),
                Arguments.arguments("  ", 1L, "jane.doe@gmail.com", "password1234"),
                // Invalid Role Id
                Arguments.arguments("Jane Doe", null, "jane.doe@gmail.com", "password1234"),
                Arguments.arguments("Jane Doe", 0L, "jane.doe@gmail.com", "password1234"),
                // Invalid Email
                Arguments.arguments("Jane Doe", 1L, null, "password1234"),
                Arguments.arguments("Jane Doe", 1L, "", "password1234"),
                Arguments.arguments("Jane Doe", 1L, " ", "password1234"),
                Arguments.arguments("Jane Doe", 1L, "mockemailaddress123456789012345678901234567890@example.com", "password1234"),
                Arguments.arguments("Jane Doe", 1L, "invalid_email.missingdotcom", "password1234"),
                // Invalid Password
                Arguments.arguments("Jane Doe", 1L, "jane.doe@gmail.com", null),
                Arguments.arguments("Jane Doe", 1L, "jane.doe@gmail.com", ""),
                Arguments.arguments("Jane Doe", 1L, "jane.doe@gmail.com", "  "),
                Arguments.arguments("Jane Doe", 1L, "jane.doe@gmail.com", "Sunny day, cool breeze, coffee.")
                );
    }
}
