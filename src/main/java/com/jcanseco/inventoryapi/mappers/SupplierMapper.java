package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDetailsDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDto entityToDto(Supplier supplier);
    SupplierDetailsDto entityToDetailsDto(Supplier supplier);
    @Mapping(target = "id", ignore = true)
    Supplier createDtoToEntity(CreateSupplierDto dto);
    AddressDto addressToDto(Address address);

    Address dtoToAddress(AddressDto dto);

    default PagedList<SupplierDto> pageToPagedList(Page<Supplier> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
