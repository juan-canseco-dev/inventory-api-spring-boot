package com.jcanseco.inventoryapi.customers.mapping;

import com.jcanseco.inventoryapi.customers.dto.CreateCustomerDto;
import com.jcanseco.inventoryapi.customers.dto.CustomerDetailsDto;
import com.jcanseco.inventoryapi.customers.dto.CustomerDto;
import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.shared.address.Address;
import com.jcanseco.inventoryapi.shared.address.AddressDto;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;





@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto entityToDto(Customer customer);

    CustomerDetailsDto entityToDetailsDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    Customer createDtoToEntity(CreateCustomerDto dto);

    AddressDto addressToDto(Address address);

    Address dtoToAddress(AddressDto dto);

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






