package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.CustomerAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto entityToDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    Customer createDtoToEntity(CreateCustomerDto dto);

    AddressDto addressToDto(CustomerAddress address);

    CustomerAddress dtoToAddress(AddressDto dto);
}
