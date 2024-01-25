package com.jcanseco.inventoryapi.validation.orders;

import com.jcanseco.inventoryapi.dtos.orders.GetOrdersRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetOrdersValidationTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("validArguments")
    public void getOrdersRequestWhenRequestIsValidValidationShouldNotFail(Integer pageNumber,
                                                                             Integer pageSize,
                                                                             String orderBy,
                                                                             String sortOrder,
                                                                             Long customerId) {
        var dto = GetOrdersRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .sortOrder(sortOrder)
                .customerId(customerId)
                .build();

        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    public void getOrdersRequestWhenRequestIsInvalidValidationShouldFail(Integer pageNumber,
                                                                            Integer pageSize,
                                                                            String orderBy,
                                                                            String sortOrder,
                                                                            Long customerId) {
        var dto = GetOrdersRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .sortOrder(sortOrder)
                .customerId(customerId)
                .build();

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    private static Stream<Arguments> validArguments() {
        return Stream.of(
                Arguments.arguments(null, null, null, null, null),
                Arguments.arguments(1, 1, "id", "asc", 1L),
                Arguments.arguments(1, 1, "customer", "ascending" , 1L),
                Arguments.arguments(1, 1, "orderedAt", "desc", 1L),
                Arguments.arguments(1, 1, "deliveredAt", "descending", 1L),
                Arguments.arguments(1, 1, "delivered", "descending", 1L),
                Arguments.arguments(1, 1, "total", "descending", 1L)
        );
    }

    private static Stream<Arguments> invalidArguments() {
        return Stream.of(
                Arguments.arguments(0, 1, "id", "asc", 1L),
                Arguments.arguments(1, 0, "id", "asc", 1L),
                Arguments.arguments(1, 1, "name", "asc", 1L),
                Arguments.arguments(1, 1, "id", "as", 1L),
                Arguments.arguments(1, 1, "id", "asc", 0L)
        );
    }

}
