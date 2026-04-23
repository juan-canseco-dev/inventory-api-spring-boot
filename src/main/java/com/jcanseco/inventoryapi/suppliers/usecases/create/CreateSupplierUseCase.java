package com.jcanseco.inventoryapi.suppliers.usecases.create;

import com.jcanseco.inventoryapi.suppliers.dto.CreateSupplierDto;
import com.jcanseco.inventoryapi.suppliers.mapping.SupplierMapper;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSupplierUseCase {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public Long execute(CreateSupplierDto dto) {
        var supplier = supplierMapper.createDtoToEntity(dto);
        var newSupplier = supplierRepository.saveAndFlush(supplier);
        return newSupplier.getId();
    }
}

