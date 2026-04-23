package com.jcanseco.inventoryapi.inventory.validation;

import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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






