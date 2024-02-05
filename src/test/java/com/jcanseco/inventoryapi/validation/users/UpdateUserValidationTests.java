package com.jcanseco.inventoryapi.validation.users;

import com.jcanseco.inventoryapi.security.dtos.users.UpdateUserDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void updateUserWhenAllPropertiesArePresentValidationShouldNotFail() {
        var dto = UpdateUserDto.builder()
                .userId(1L)
                .fullName("Jane Doe")
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateDto")
    public void updateUserWhenPropertiesAreInvalidValidationShouldFail(Long userId, String fullName) {
        var dto = UpdateUserDto.builder()
                .userId(userId)
                .fullName(fullName)
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    private static Stream<Arguments> invalidUpdateDto() {
        return Stream.of(
                Arguments.arguments(0L, "Jane Doe"),
                Arguments.arguments(null, "Jane Doe"),
                Arguments.arguments(1L, ""),
                Arguments.arguments(1L, null),
                Arguments.arguments(1L, "  ")
        );
    }
}
