package com.jcanseco.inventoryapi.security.validation.users;

import com.jcanseco.inventoryapi.security.dtos.users.GetUsersRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;


public class GetUsersValidationTests {
    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("validDto")
    public void getUsersWhenPropertiesAreValidValidationShouldNotFail(Integer pageNumber,
                                                                      Integer pageSize,
                                                                      String orderBy,
                                                                      String sortOrder,
                                                                      String fullName,
                                                                      String email) {
        var request = GetUsersRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .sortOrder(sortOrder)
                .fullName(fullName)
                .email(email)
                .build();

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void getUsersRequestWhenPropertiesAreInvalidValidationShouldFail(Integer pageNumber,
                                                                            Integer pageSize,
                                                                            String orderBy,
                                                                            String sortOrder,
                                                                            String fullName,
                                                                            String email) {
        var request = GetUsersRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .sortOrder(sortOrder)
                .fullName(fullName)
                .email(email)
                .build();

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    private static Stream<Arguments> validDto() {
        return Stream.of(
                Arguments.arguments(null, null, null, null, null, null),
                Arguments.arguments(1, 1, "id", "asc", "doe", "smith@mail.com"),
                Arguments.arguments(1, 1, "fullName", "ascending", "doe", "smith@mail.com"),
                Arguments.arguments(1, 1, "email", "desc", "doe", "smith@mail.com"),
                Arguments.arguments(1, 1, "createdAt", "descending", "doe", "smith@mail.com"),
                Arguments.arguments(1, 1, "updatedAt", "asc", "doe", "smith@mail.com")
        );
    }

    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.arguments(0, 1, "id", "asc", "doe", "smith@mail.com"),
                Arguments.arguments(-1, 1, "id", "asc", "doe", "smith@mail.com"),
                Arguments.arguments(1, 0, "id", "asc", "doe", "smith@mail.com"),
                Arguments.arguments(1, -1, "id", "asc", "doe", "smith@mail.com"),
                Arguments.arguments(1, 1, "name", "asc", "doe", "smith@mail.com"),
                Arguments.arguments(1, 1, "name", "ascendingg", "doe", "smith@mail.com")
        );
    }
}
