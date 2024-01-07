package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.mappers.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerMapperTests {

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    public void addressDtoToEntity() {

        var dto = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var address = mapper.dtoToAddress(dto);
        assertNotNull(address);

        assertEquals(dto.getCountry(), address.getCountry());
        assertEquals(dto.getState(), address.getState());
        assertEquals(dto.getCity(), address.getCity());
        assertEquals(dto.getZipCode(), address.getZipCode());
        assertEquals(dto.getStreet(), address.getStreet());
    }

    @Test
    public void addressEntityToDto() {

        var address = newCustomerAddress(
                "Mexico",
                "Sonora",
                "Hermosillo",
                "83200",
                "Center"
        );

        var dto = mapper.addressToDto(address);
        assertNotNull(dto);

        assertEquals(address.getCountry(), dto.getCountry());
        assertEquals(address.getState(), dto.getState());
        assertEquals(address.getCity(), dto.getCity());
        assertEquals(address.getZipCode(), dto.getZipCode());
        assertEquals(address.getStreet(), dto.getStreet());
    }

    @Test
    public void createDtoToEntity() {

        var address = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var createDto = CreateCustomerDto.builder()
                .dni("X1Y9Z3A7B2C8D6E0F5G4")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(address)
                .build();

        var customer = mapper.createDtoToEntity(createDto);
        assertNotNull(customer);

        assertEquals(createDto.getDni(), customer.getDni());
        assertEquals(createDto.getFullName(), customer.getFullName());
        assertEquals(createDto.getPhone(), customer.getPhone());

        assertEquals(address.getCountry(), customer.getAddress().getCountry());
        assertEquals(address.getState(), customer.getAddress().getState());
        assertEquals(address.getCity(), customer.getAddress().getCity());
        assertEquals(address.getZipCode(), customer.getAddress().getZipCode());
        assertEquals(address.getStreet(), customer.getAddress().getStreet());
    }

    @Test
    public void entityToDto() {

        var customer = newCustomer(
                1L,
                "X1Y9Z3A7B2C8D6E0F5G4",
                "John Doe",
                "555-1234-1",
                newCustomerAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        var dto = mapper.entityToDto(customer);
        assertNotNull(dto);

        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getDni(), dto.getDni());
        assertEquals(customer.getPhone(), dto.getPhone());
        assertEquals(customer.getFullName(), dto.getFullName());
    }

    @Test
    public void entityToDetailsDto() {

        var address = newCustomerAddress(
                "Mexico",
                "Sonora",
                "Hermosillo",
                "83200",
                "Center"
        );

        var customer = newCustomer(
                1L,
                "X1Y9Z3A7B2C8D6E0F5G4",
                "John Doe",
                "555-1234-1",
                address
        );

        var dto = mapper.entityToDetailsDto(customer);
        assertNotNull(dto);

        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getDni(), dto.getDni());
        assertEquals(customer.getPhone(), dto.getPhone());
        assertEquals(customer.getFullName(), dto.getFullName());

        assertEquals(address.getCountry(), dto.getAddress().getCountry());
        assertEquals(address.getState(), dto.getAddress().getState());
        assertEquals(address.getCity(), dto.getAddress().getCity());
        assertEquals(address.getZipCode(), dto.getAddress().getZipCode());
        assertEquals(address.getStreet(), dto.getAddress().getStreet());
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var customers = List.of(
                newCustomer(1L,
                        "123456789",
                        "555-1234-1",
                        "John Doe",
                        newCustomerAddress(
                                "United States",
                                "California",
                                "San Francisco",
                                "94105",
                                "123 Main St")
                ),
                newCustomer(2L,
                        "987654321",
                        "555-1234-2",
                        "Jane Smith",
                        newCustomerAddress("United Kingdom",
                                "England",
                                "London",
                                "EC1A 1BB",
                                "456 High St")
                )
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
