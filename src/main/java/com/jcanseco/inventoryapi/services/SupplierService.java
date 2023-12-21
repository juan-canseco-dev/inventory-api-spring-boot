package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.*;
import java.util.List;

public interface SupplierService {
    Long createSupplier(CreateSupplierDto dto);
    void updateSupplier(UpdateSupplierDto dto);
    void deleteSupplier(Long supplierId);
    SupplierDetailsDto getSupplierById(Long supplierId);
    List<SupplierDto> getSuppliers(GetSuppliersRequest request);
    PagedList<SupplierDto> getSuppliersPaged(GetSuppliersRequest request);
}
