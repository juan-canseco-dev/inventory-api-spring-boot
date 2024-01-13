package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.GetSuppliersRequest;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDetailsDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;
import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.SupplierMapper;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.services.SupplierService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class SupplierServiceUnitTests {

    @Mock
    private SupplierRepository supplierRepository;
    @Spy
    private SupplierMapper supplierMapper = Mappers.getMapper(SupplierMapper.class);
    @Spy
    private IndexUtility indexUtility = new IndexUtility();
    @InjectMocks
    private SupplierService supplierService;
    private List<Supplier> suppliers;
    private AddressDto defaultAddressDto;
    private Address defaultAddress;

    @BeforeEach
    public void setup() {

        suppliers = List.of(
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

        defaultAddressDto = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        defaultAddress = Address.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

    }

    @Test
    public void createSupplierCreateShouldBeSuccessful() {

        var createdSupplierId = 1L;

        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddressDto)
                .build();

        var mappedSupplier = Supplier.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddress)
                .build();

        var newSupplier = Supplier.builder()
                .id(createdSupplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddress)
                .build();

        when(supplierMapper.createDtoToEntity(dto)).thenReturn(mappedSupplier);
        when(supplierRepository.saveAndFlush(mappedSupplier)).thenReturn(newSupplier);

        var resultSupplierId = supplierService.createSupplier(dto);
        assertEquals(createdSupplierId, resultSupplierId);
    }

    @Test
    public void updateSupplierWhenSupplierExistsUpdateShouldBeSuccessful() {

        var supplierId = 1L;

        var updatedAddressDto = AddressDto.builder()
                .country("US")
                .state("Colorado")
                .city("Denver")
                .zipCode("80014")
                .street("Colorado's Gateway")
                .build();

        var dto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp Inc")
                .contactName("John Doe Smith Jr")
                .contactPhone("555-1234-9")
                .address(updatedAddressDto)
                .build();

        var foundSupplier = Supplier.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddress)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(foundSupplier));
        supplierService.updateSupplier(dto);

        verify(supplierRepository, times(1)).saveAndFlush(foundSupplier);

        var supplierArgCaptor = ArgumentCaptor.forClass(Supplier.class);

        verify(supplierRepository).saveAndFlush(supplierArgCaptor.capture());
        var updatedSupplier = supplierArgCaptor.getValue();
        assertNotNull(updatedSupplier);

        assertEquals(supplierId, updatedSupplier.getId());

        assertEquals(dto.getCompanyName(), updatedSupplier.getCompanyName());
        assertEquals(dto.getContactName(), updatedSupplier.getContactName());
        assertEquals(dto.getContactPhone(), updatedSupplier.getContactPhone());
        assertEquals(dto.getAddress().getCountry(), updatedSupplier.getAddress().getCountry());
        assertEquals(dto.getAddress().getState(), updatedSupplier.getAddress().getState());
        assertEquals(dto.getAddress().getCity(), updatedSupplier.getAddress().getCity());
        assertEquals(dto.getAddress().getZipCode(), updatedSupplier.getAddress().getZipCode());
        assertEquals(dto.getAddress().getStreet(), updatedSupplier.getAddress().getStreet());
    }

    @Test
    public void updateSupplierWhenSupplierDoNotExistsShouldThrowNotFoundException() {

        var supplierId = 1L;

        var updatedAddressDto = AddressDto.builder()
                .country("US")
                .state("Colorado")
                .city("Denver")
                .zipCode("80014")
                .street("Colorado's Gateway")
                .build();

        var dto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp Inc")
                .contactName("John Doe Smith Jr")
                .contactPhone("555-1234-9")
                .address(updatedAddressDto)
                .build();

        when(supplierRepository.findById(supplierId)).thenThrow(new NotFoundException("Supplier Not Found"));
        assertThrows(NotFoundException.class, () -> supplierService.updateSupplier(dto));
    }

    @Test
    public void deleteSupplierWhenSupplierExistsDeleteShouldBeSuccessful() {

        var supplierId = 1L;

        var foundSupplier = Supplier.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddress)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(foundSupplier));
        doNothing().when(supplierRepository).delete(foundSupplier);
        supplierService.deleteSupplier(supplierId);
        verify(supplierRepository, times(1)).delete(foundSupplier);
    }

    @Test
    public void deleteSupplierWhenSupplierDoNotExistsShouldThrowNotFoundException() {
        var supplierId = 1L;
        when(supplierRepository.findById(supplierId)).thenThrow(new NotFoundException("Supplier Not Found"));
        assertThrows(NotFoundException.class, () -> supplierService.deleteSupplier(supplierId));
    }

    @Test
    public void getSupplierWhenSupplierExistsGetShouldBeSuccessful() {

        var supplierId = 1L;

        var foundSupplier = Supplier.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddress)
                .build();

        var supplierDto = SupplierDetailsDto.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddressDto)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(foundSupplier));
        when(supplierMapper.entityToDetailsDto(foundSupplier)).thenReturn(supplierDto);

        var resultDto = supplierService.getSupplierById(supplierId);
        assertEquals(supplierDto, resultDto);
    }

    @Test
    public void getSupplierWhenSupplierDoNotExistsShouldThrowNotFoundException() {
        var supplierId = 1L;
        when(supplierRepository.findById(supplierId)).thenThrow(new NotFoundException("Supplier Not Found"));
        assertThrows(NotFoundException.class, () -> supplierService.getSupplierById(supplierId));
    }

    @Test
    public void getSuppliersShouldReturnList() {

        var expectedResult = suppliers.stream()
                .map(supplierMapper::entityToDto)
                .toList();

        var request = GetSuppliersRequest.builder().build();
        Specification<Supplier> mockSpec = any();
        when(supplierRepository.findAll(mockSpec)).thenReturn(suppliers);
        var result = supplierService.getSuppliers(request);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertThat(result).hasSameElementsAs(expectedResult);
    }

    @Test
    public void getSuppliersPageShouldReturnPagedList() {

        var totalSuppliersInDb = 4;

        var totalPages = 2;

        var expectedItems = suppliers.stream()
                .map(supplierMapper::entityToDto)
                .toList();


        var request = GetSuppliersRequest.builder()
                .pageNumber(1)
                .pageSize(2)
                .build();

        Specification<Supplier> mockSpec = any();
        PageRequest mockPageRequest = any();
        Page<Supplier> mockPage = new PageImpl<>(
                suppliers,
                Pageable.ofSize(2),
                totalSuppliersInDb
        );

        when(supplierRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = supplierService.getSuppliersPaged(request);
        assertNotNull(pagedList);
        assertEquals(request.getPageNumber(), pagedList.getPageNumber());
        assertEquals(request.getPageSize(), pagedList.getPageSize());
        assertEquals(totalSuppliersInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(expectedItems);
    }
}
