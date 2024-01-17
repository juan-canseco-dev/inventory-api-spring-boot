package com.jcanseco.inventoryapi.validators.allproductsexist;

import com.jcanseco.inventoryapi.repositories.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AllProductsExistValidator implements ConstraintValidator<AllProductsExist, Map<Long, Long>> {

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(Map<Long, Long> productsWithQuantities, ConstraintValidatorContext context) {

        if (productsWithQuantities == null) {
            return true;
        }

        for (Long productId : productsWithQuantities.keySet()) {
            if (!productRepository.existsById(productId)) {
                return false;
            }
        }

        return true;
    }
}
