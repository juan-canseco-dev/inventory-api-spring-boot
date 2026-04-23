package com.jcanseco.inventoryapi.suppliers.usecases.update;

import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import com.jcanseco.inventoryapi.suppliers.dto.UpdateSupplierDto;
import com.jcanseco.inventoryapi.suppliers.mapping.SupplierMapper;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSupplierUseCase {

    private static final String NOT_FOUND_MESSAGE = "Supplier with the Id {%d} was not found.";

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public void execute(UpdateSupplierDto dto) {
        var supplier = supplierRepository
                .findById(dto.getSupplierId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, dto.getSupplierId())));

        supplier.setCompanyName(dto.getCompanyName());
        supplier.setContactName(dto.getContactName());
        supplier.setContactPhone(dto.getContactPhone());
        supplier.setAddress(supplierMapper.dtoToAddress(dto.getAddress()));

        supplierRepository.saveAndFlush(supplier);
    }
}

