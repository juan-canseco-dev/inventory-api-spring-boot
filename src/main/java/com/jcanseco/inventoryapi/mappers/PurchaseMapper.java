package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.products.ProductDto;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseDetailsDto;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseDto;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseItemDto;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Purchase;
import com.jcanseco.inventoryapi.entities.PurchaseItem;
import com.jcanseco.inventoryapi.entities.Supplier;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseDto entityToDto(Purchase purchase);
    PurchaseDetailsDto entityToDetailsDto(Purchase purchase);
    default String supplierToName(Supplier supplier) {
        return supplier.getCompanyName();
    }
    PurchaseItemDto itemToDto(PurchaseItem item);

    default PagedList<PurchaseDto> pageToPagedList(Page<Purchase> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

}
