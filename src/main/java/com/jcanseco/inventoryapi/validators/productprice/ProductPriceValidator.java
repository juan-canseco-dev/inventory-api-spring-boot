package com.jcanseco.inventoryapi.validators.productprice;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class ProductPriceValidator implements ConstraintValidator<ProductPrice, Object> {
    private String purchasePrice;
    private String salePrice;

    @Override
    public void initialize(ProductPrice productPrice) {
        this.purchasePrice = productPrice.purchasePrice();
        this.salePrice = productPrice.salePrice();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        Double purchasePriceValue = (Double) new BeanWrapperImpl(value).getPropertyValue(purchasePrice);
        Double salePriceValue = (Double) new BeanWrapperImpl(value).getPropertyValue(salePrice);
        Objects.requireNonNull(salePriceValue);
        Objects.requireNonNull(purchasePriceValue);
        return salePriceValue > purchasePriceValue;
    }
}
