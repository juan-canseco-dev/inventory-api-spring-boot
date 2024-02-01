package com.jcanseco.inventoryapi.validation.roles;

import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class CreateRoleValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createRoleDtoWhenDtoIsValidValidationShouldNotFail() {
        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Roles."
                ))
                .build();
        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void createRoleDtoWhenDtoIsInvalidValidationShouldFail(String name, List<String> permissions) {
        var dto = CreateRoleDto.builder()
                .name(name)
                .permissions(permissions)
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.arguments(null , List.of("Permissions.Roles.View")),
                Arguments.arguments("" , List.of("Permissions.Roles.View")),
                Arguments.arguments(" " , List.of("Permissions.Roles.View")),
                Arguments.arguments("New Role" , List.of()),
                Arguments.arguments("New Role", List.of("Permissions.Roles.View", "Permissions.Roles.View"))
        );
    }
}
