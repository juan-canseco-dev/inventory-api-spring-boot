package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.mappers.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerMapperTests {
    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);
    private Address defaultAddress;
    private AddressDto defaultAddressDto;

    @BeforeEach
    public void setup() {
        defaultAddress = Address.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        defaultAddressDto = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    @Test
    public void addressDtoToEntity() {

        var address = mapper.dtoToAddress(defaultAddressDto);
        assertNotNull(address);

        assertEquals(defaultAddressDto.getCountry(), address.getCountry());
        assertEquals(defaultAddressDto.getState(), address.getState());
        assertEquals(defaultAddressDto.getCity(), address.getCity());
        assertEquals(defaultAddressDto.getZipCode(), address.getZipCode());
        assertEquals(defaultAddressDto.getStreet(), address.getStreet());
    }

    @Test
    public void addressEntityToDto() {

        var dto = mapper.addressToDto(defaultAddress);
        assertNotNull(dto);

        assertEquals(defaultAddress.getCountry(), dto.getCountry());
        assertEquals(defaultAddress.getState(), dto.getState());
        assertEquals(defaultAddress.getCity(), dto.getCity());
        assertEquals(defaultAddress.getZipCode(), dto.getZipCode());
        assertEquals(defaultAddress.getStreet(), dto.getStreet());
    }

    @Test
    public void createDtoToEntity() {

        var createDto = CreateCustomerDto.builder()
                .dni("X1Y9Z3A7B2C8D6E0F5G4")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddressDto)
                .build();

        var customer = mapper.createDtoToEntity(createDto);
        assertNotNull(customer);

        assertEquals(createDto.getDni(), customer.getDni());
        assertEquals(createDto.getFullName(), customer.getFullName());
        assertEquals(createDto.getPhone(), customer.getPhone());

        assertEquals(defaultAddressDto.getCountry(), customer.getAddress().getCountry());
        assertEquals(defaultAddressDto.getState(), customer.getAddress().getState());
        assertEquals(defaultAddressDto.getCity(), customer.getAddress().getCity());
        assertEquals(defaultAddressDto.getZipCode(), customer.getAddress().getZipCode());
        assertEquals(defaultAddressDto.getStreet(), customer.getAddress().getStreet());
    }

    @Test
    public void entityToDto() {

        var customer = Customer.builder()
                .id(1L)
                .dni("X1Y9Z3A7B2C8D6E0F5G4")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress)
                .build();


        var dto = mapper.entityToDto(customer);
        assertNotNull(dto);

        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getDni(), dto.getDni());
        assertEquals(customer.getPhone(), dto.getPhone());
        assertEquals(customer.getFullName(), dto.getFullName());
    }

    @Test
    public void entityToDetailsDto() {

        var customer = Customer.builder()
                .id(1L)
                .dni("X1Y9Z3A7B2C8D6E0F5G4")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress)
                .build();


        var dto = mapper.entityToDetailsDto(customer);
        assertNotNull(dto);

        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getDni(), dto.getDni());
        assertEquals(customer.getPhone(), dto.getPhone());
        assertEquals(customer.getFullName(), dto.getFullName());

        assertEquals(defaultAddress.getCountry(), dto.getAddress().getCountry());
        assertEquals(defaultAddress.getState(), dto.getAddress().getState());
        assertEquals(defaultAddress.getCity(), dto.getAddress().getCity());
        assertEquals(defaultAddress.getZipCode(), dto.getAddress().getZipCode());
        assertEquals(defaultAddress.getStreet(), dto.getAddress().getStreet());
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var customers = List.of(
                Customer.builder()
                        .id(1L)
                        .dni("123456789")
                        .fullName("John Doe")
                        .phone("555-1234-1")
                        .address(
                                Address.builder()
                                        .country("United States")
                                        .state("California")
                                        .city("San Francisco")
                                        .zipCode("94105")
                                        .street("123 Main St")
                                        .build()
                        )
                        .build(),
                Customer.builder()
                        .id(2L)
                        .dni("987654321")
                        .fullName("Jane Smith")
                        .phone("555-1234-2")
                        .address(
                                Address.builder()
                                        .country("United Kingdom")
                                        .state("England")
                                        .city("London")
                                        .zipCode("94106")
                                        .street("456 High St")
                                        .build()
                        )
                        .build()
        );

        var customersDto = customers.stream().map(mapper::entityToDto).toList();

        Page<Customer> page = new PageImpl<>(customers, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(customersDto);

    }
}
