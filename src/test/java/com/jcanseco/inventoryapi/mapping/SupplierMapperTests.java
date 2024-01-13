package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.mappers.SupplierMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SupplierMapperTests {
    private final SupplierMapper mapper = Mappers.getMapper(SupplierMapper.class);
    private Address address;
    private AddressDto addressDto;
    @BeforeEach
    public void setup() {
        addressDto = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
        address = Address
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    @Test
    public void addressDtoToEntity() {

        var dto = addressDto;

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
        var dto = addressDto;
        var createDto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(dto)
                .build();

        var supplier = mapper.createDtoToEntity(createDto);
        assertNotNull(supplier);

        assertEquals(createDto.getCompanyName(), supplier.getCompanyName());
        assertEquals(createDto.getContactName(), supplier.getContactName());
        assertEquals(createDto.getContactPhone(), supplier.getContactPhone());

        assertEquals(dto.getCountry(), supplier.getAddress().getCountry());
        assertEquals(dto.getState(), supplier.getAddress().getState());
        assertEquals(dto.getCity(), supplier.getAddress().getCity());
        assertEquals(dto.getZipCode(), supplier.getAddress().getZipCode());
        assertEquals(dto.getStreet(), supplier.getAddress().getStreet());
    }

    @Test
    public void entityToDto() {

        var supplier = Supplier.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();


        var dto = mapper.entityToDto(supplier);
        assertNotNull(dto);

        assertEquals(supplier.getId(), dto.getId());
        assertEquals(supplier.getCompanyName(), dto.getCompanyName());
        assertEquals(supplier.getContactName(), dto.getContactName());
        assertEquals(supplier.getContactPhone(), dto.getContactPhone());
    }

    @Test
    public void entityToDetailsDto() {

        var supplier = Supplier.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

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
                Supplier.builder()
                        .id(1L)
                        .companyName("ABC Corp")
                        .contactName("John Doe")
                        .contactPhone("555-1234-1")
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
                Supplier.builder()
                        .id(2L)
                        .companyName("XYZ Ltd")
                        .contactName("Jane Smith")
                        .contactPhone("555-1234-2")
                        .address(
                                Address.builder()
                                        .country("United Kingdom")
                                        .state("England")
                                        .city("London")
                                        .zipCode("EC1A 1BB")
                                        .street("456 High St")
                                        .build()
                        )
                        .build()
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
