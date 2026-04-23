package com.jcanseco.inventoryapi.suppliers.mapping;

import com.jcanseco.inventoryapi.shared.address.Address;
import com.jcanseco.inventoryapi.shared.address.AddressDto;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.suppliers.dto.CreateSupplierDto;
import com.jcanseco.inventoryapi.suppliers.dto.SupplierDetailsDto;
import com.jcanseco.inventoryapi.suppliers.dto.SupplierDto;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
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






