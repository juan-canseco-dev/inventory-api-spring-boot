package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.products.ProductDetailsDto;
import com.jcanseco.inventoryapi.dtos.products.ProductDto;
import com.jcanseco.inventoryapi.entities.*;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto entityToDto(Product product);
    default String supplierToName(Supplier supplier) {
        return supplier.getCompanyName();
    }
    default String categoryToName(Category category) {
        return category.getName();
    }

    default Long stockToLong(Stock stock) {
        return stock.getQuantity();
    }

    default String unitToName(UnitOfMeasurement unit) {
        return unit.getName();
    }
    ProductDetailsDto entityToDetailsDto(Product product);
    default PagedList<ProductDto> pageToPagedList(Page<Product> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
