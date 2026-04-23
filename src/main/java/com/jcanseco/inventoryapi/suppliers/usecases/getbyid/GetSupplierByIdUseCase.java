package com.jcanseco.inventoryapi.suppliers.usecases.getbyid;

import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import com.jcanseco.inventoryapi.suppliers.dto.SupplierDetailsDto;
import com.jcanseco.inventoryapi.suppliers.mapping.SupplierMapper;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSupplierByIdUseCase {

    private static final String NOT_FOUND_MESSAGE = "Supplier with the Id {%d} was not found.";

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierDetailsDto execute(Long supplierId) {
        return supplierRepository
                .findById(supplierId)
                .map(supplierMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, supplierId)));
    }
}

