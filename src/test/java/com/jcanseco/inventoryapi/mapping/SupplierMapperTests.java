package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.mappers.SupplierMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SupplierMapperTests {
    private final SupplierMapper mapper = Mappers.getMapper(SupplierMapper.class);

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

        var address = newSupplierAddress(
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

        var createDto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var supplier = mapper.createDtoToEntity(createDto);
        assertNotNull(supplier);

        assertEquals(createDto.getCompanyName(), supplier.getCompanyName());
        assertEquals(createDto.getContactName(), supplier.getContactName());
        assertEquals(createDto.getContactPhone(), supplier.getContactPhone());

        assertEquals(address.getCountry(), supplier.getAddress().getCountry());
        assertEquals(address.getState(), supplier.getAddress().getState());
        assertEquals(address.getCity(), supplier.getAddress().getCity());
        assertEquals(address.getZipCode(), supplier.getAddress().getZipCode());
        assertEquals(address.getStreet(), supplier.getAddress().getStreet());
    }

    @Test
    public void entityToDto() {

        var supplier = newSupplier(
                1L,
                "ABC Corp",
                "John Doe",
                "555-1234-1",
                newSupplierAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        var dto = mapper.entityToDto(supplier);
        assertNotNull(dto);

        assertEquals(supplier.getId(), dto.getId());
        assertEquals(supplier.getCompanyName(), dto.getCompanyName());
        assertEquals(supplier.getContactName(), dto.getContactName());
        assertEquals(supplier.getContactPhone(), dto.getContactPhone());
    }

    @Test
    public void entityToDetailsDto() {

        var address = newSupplierAddress(
                "Mexico",
                "Sonora",
                "Hermosillo",
                "83200",
                "Center"
        );

        var supplier = newSupplier(
                1L,
                "ABC Corp",
                "John Doe",
                "555-1234-1",
                address
        );

        var dto = mapper.entityToDetailsDto(supplier);
        assertNotNull(dto);

        assertEquals(supplier.getId(), dto.getId());
        assertEquals(supplier.getCompanyName(), dto.getCompanyName());
        assertEquals(supplier.getContactName(), dto.getContactName());
        assertEquals(supplier.getContactPhone(), dto.getContactPhone());

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

        var suppliers = List.of(
                newSupplier(
                        "ABC Corp",
                        "John Doe",
                        "555-1234-1",
                        newSupplierAddress(
                                "United States",
                                "California",
                                "San Francisco",
                                "94105",
                                "123 Main St")
                ),
                newSupplier(
                        "XYZ Ltd",
                        "Jane Smith",
                        "555-1234-2",
                        newSupplierAddress("United Kingdom",
                                "England",
                                "London",
                                "EC1A 1BB",
                                "456 High St"))
        );

        var suppliersDto = suppliers.stream().map(mapper::entityToDto).toList();

        Page<Supplier> page = new PageImpl<>(suppliers, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(suppliersDto);
    }
}
