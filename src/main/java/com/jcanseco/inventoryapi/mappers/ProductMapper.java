package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.products.ProductDetailsDto;
import com.jcanseco.inventoryapi.dtos.products.ProductDto;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto entityToDto(Product product);
    default String supplierToName(Supplier supplier) {
        return supplier.getCompanyName();
    }
    default String categoryToName(Category category) {
        return category.getName();
    }

    default String unitToName(UnitOfMeasurement unit) {
        return unit.getName();
    }
    ProductDetailsDto entityToDetailsDto(Product product);
}
