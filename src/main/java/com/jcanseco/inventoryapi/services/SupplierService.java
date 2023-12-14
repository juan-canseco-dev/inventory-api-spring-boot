package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.GetSuppliersRequest;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;

import java.util.List;

public interface SupplierService {
    SupplierDto createSupplier(CreateSupplierDto dto);
    SupplierDto updateSupplier(UpdateSupplierDto dto);
    void deleteSupplier(Long supplierId);
    SupplierDto getSupplierById(Long supplierId);
    List<SupplierDto> getSuppliers(GetSuppliersRequest request);
    PagedList<SupplierDto> getSuppliersPaged(GetSuppliersRequest request);
}
