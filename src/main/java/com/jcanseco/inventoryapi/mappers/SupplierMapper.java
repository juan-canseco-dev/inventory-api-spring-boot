package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDetailsDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDto entityToDto(Supplier supplier);
    SupplierDetailsDto entityToDetailsDto(Supplier supplier);
    @Mapping(target = "id", ignore = true)
    Supplier createDtoToEntity(CreateSupplierDto dto);
    AddressDto addressToDto(SupplierAddress address);
    SupplierAddress dtoToAddress(AddressDto dto);
}
