package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDetailsDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.CustomerAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto entityToDto(Customer customer);

    CustomerDetailsDto entityToDetailsDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    Customer createDtoToEntity(CreateCustomerDto dto);

    AddressDto addressToDto(CustomerAddress address);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    CustomerAddress dtoToAddress(AddressDto dto);

    default PagedList<CustomerDto> pageToPagedList(Page<Customer> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
