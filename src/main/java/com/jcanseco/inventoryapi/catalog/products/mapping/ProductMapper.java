package com.jcanseco.inventoryapi.catalog.products.mapping;

import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.catalog.products.dto.ProductDetailsDto;
import com.jcanseco.inventoryapi.catalog.products.dto.ProductDto;
import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.units.domain.UnitOfMeasurement;
import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
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






