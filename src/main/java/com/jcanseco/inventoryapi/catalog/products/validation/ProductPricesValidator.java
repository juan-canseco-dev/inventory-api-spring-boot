package com.jcanseco.inventoryapi.catalog.products.validation;

import com.jcanseco.inventoryapi.catalog.products.dto.ProductPricesDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductPricesValidator implements ConstraintValidator<ValidProductPrices, ProductPricesDto> {

    @Override
    public boolean isValid(ProductPricesDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        if (dto.getPurchasePrice() == null || dto.getSalePrice() == null) {
            return true;
        }

        boolean valid = dto.getSalePrice() > dto.getPurchasePrice();
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Sale price must be greater than purchase price"
                    ).addPropertyNode("salePrice")
                    .addConstraintViolation();
        }

        return valid;
    }
}







