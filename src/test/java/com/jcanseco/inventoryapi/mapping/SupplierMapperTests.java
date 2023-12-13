package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
import com.jcanseco.inventoryapi.mappers.SupplierMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
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

        var address = SupplierAddress
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

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
        var supplierId = 1L;

        var address = SupplierAddress
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var supplier = Supplier.builder()
                .id(supplierId)
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

        assertEquals(address.getCountry(), dto.getAddress().getCountry());
        assertEquals(address.getState(), dto.getAddress().getState());
        assertEquals(address.getCity(), dto.getAddress().getCity());
        assertEquals(address.getZipCode(), dto.getAddress().getZipCode());
        assertEquals(address.getStreet(), dto.getAddress().getStreet());
    }
}
