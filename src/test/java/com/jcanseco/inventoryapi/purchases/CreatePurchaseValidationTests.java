package com.jcanseco.inventoryapi.purchases;

import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.inventory.validation.AllProductsExistValidator;
import com.jcanseco.inventoryapi.purchases.dto.CreatePurchaseDto;
import com.jcanseco.inventoryapi.shared.testing.CustomLocalValidatorFactoryBean;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreatePurchaseValidationTests {
    @Mock
    private ProductRepository productRepository;

    private Validator validator;

    @BeforeEach
    public void setup() {

        List<ConstraintValidator<?,?>> customConstraintValidators = Collections.singletonList(
                new AllProductsExistValidator(productRepository)
        );
        ValidatorFactory customValidatorFactory = new CustomLocalValidatorFactoryBean(customConstraintValidators);
        validator = customValidatorFactory.getValidator();
    }

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



    @Test
    public void createPurchaseDtoWhenDtoIsValidValidationShouldNotFail() {

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.existsById(2L)).thenReturn(true);

        var dto = CreatePurchaseDto.builder()
                .supplierId(1L)
                .productsWithQuantities(validProductsWithQuantities)
                .build();


        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void createPurchaseDtoWhenDtoIsInvalidValidationShouldFail(Long supplierId, HashMap<Long, Long> productsWithQuantities) {
        var dto = CreatePurchaseDto.builder()
                .supplierId(supplierId)
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






