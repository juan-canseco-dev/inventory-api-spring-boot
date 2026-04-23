package com.jcanseco.inventoryapi.purchases.mapping;

import com.jcanseco.inventoryapi.purchases.dto.PurchaseDetailsDto;
import com.jcanseco.inventoryapi.purchases.dto.PurchaseDto;
import com.jcanseco.inventoryapi.purchases.dto.PurchaseItemDto;
import com.jcanseco.inventoryapi.purchases.domain.Purchase;
import com.jcanseco.inventoryapi.purchases.domain.PurchaseItem;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
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






