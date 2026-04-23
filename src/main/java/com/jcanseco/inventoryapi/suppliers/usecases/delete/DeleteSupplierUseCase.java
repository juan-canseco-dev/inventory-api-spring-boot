package com.jcanseco.inventoryapi.suppliers.usecases.delete;

import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSupplierUseCase {

    private static final String NOT_FOUND_MESSAGE = "Supplier with the Id {%d} was not found.";

    private final SupplierRepository supplierRepository;

    public void execute(Long supplierId) {
        var supplier = supplierRepository
                .findById(supplierId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, supplierId)));

        supplierRepository.delete(supplier);
    }
}

