package com.jcanseco.inventoryapi.validation.orders;

import com.jcanseco.inventoryapi.dtos.orders.CreateOrderDto;
import com.jcanseco.inventoryapi.factories.CustomLocalValidatorFactoryBean;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.validators.allproductsexist.AllProductsExistValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CreateOrderValidationTests {
    @Mock
    private ProductRepository productRepository;
    private Validator validator;

    static final HashMap<Long, Long> validProductsWithQuantities = new HashMap<>() {{
        put(1L, 10L);
        put(2L, 20L);
    }};

    static final HashMap<Long, Long> productsWithZeroOrNegativeQuantities = new HashMap<>() {{
        put(1L, 0L);
        put(2L, -1L);
    }};

    static final HashMap<Long, Long> notExistentProductsWithQuantities = new HashMap<>() {{
        put(3L, 10L);
        put(4L, 10L);
    }};

    @BeforeEach
    public void setup() {
        List<ConstraintValidator<?,?>> customConstraintValidators = Collections.singletonList(
                new AllProductsExistValidator(productRepository)
        );
        ValidatorFactory customValidatorFactory = new CustomLocalValidatorFactoryBean(customConstraintValidators);
        validator = customValidatorFactory.getValidator();
    }

    @Test
    public void createOrderDtoWhenDtoIsValidValidationShouldNotFail() {

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.existsById(2L)).thenReturn(true);

        var dto = CreateOrderDto.builder()
                .customerId(1L)
                .productsWithQuantities(validProductsWithQuantities)
                .build();


        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void createOrderDtoWhenDtoIsInvalidValidationShouldFail(Long customerId, HashMap<Long, Long> productsWithQuantities) {
        var dto = CreateOrderDto.builder()
                .customerId(customerId)
                .productsWithQuantities(productsWithQuantities)
                .build();
        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }


    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.arguments(0L, validProductsWithQuantities),
                Arguments.arguments(1L, productsWithZeroOrNegativeQuantities),
                Arguments.arguments(1L, notExistentProductsWithQuantities)
        );
    }

}
