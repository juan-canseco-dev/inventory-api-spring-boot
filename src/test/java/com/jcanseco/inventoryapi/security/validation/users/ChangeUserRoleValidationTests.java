package com.jcanseco.inventoryapi.security.validation.users;

import com.jcanseco.inventoryapi.security.dtos.users.ChangeUserRoleDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class ChangeUserRoleValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void changeUserRoleWhenAllPropertiesAreValidValidationShouldNotFail() {
        var dto = ChangeUserRoleDto.builder()
                .userId(1L)
                .roleId(1L)
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }


    @ParameterizedTest
    @MethodSource("invalidDto")
    public void changeUserRoleWhenPropertiesAreInvalidValidationShouldFail(Long userId, Long roleId) {
        var dto = ChangeUserRoleDto.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.arguments(0L, 1L),
                Arguments.arguments(null, 1L),
                Arguments.arguments(1L, 0L),
                Arguments.arguments(1L, null)
        );
    }
}
