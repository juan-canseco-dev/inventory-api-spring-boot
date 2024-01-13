package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDetailsDto;
import com.jcanseco.inventoryapi.dtos.customers.GetCustomersRequest;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CustomerMapper;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import com.jcanseco.inventoryapi.services.CustomerService;
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
public class CustomerServiceUnitTests {

    @Mock
    private CustomerRepository customerRepository;
    @Spy
    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);
    @Spy
    private IndexUtility indexUtility = new IndexUtility();
    @InjectMocks
    private CustomerService customerService;
    private List<Customer> customers;
    private AddressDto defaultAddressDto;
    private Address defaultAddress;

    @BeforeEach
    public void setup() {
        customers = List.of(
                Customer.builder()
                        .id(1L)
                        .dni("123456789")
                        .fullName("John Doe")
                        .phone("555-1234-1")
                        .address(Address.builder()
                                .country("United States")
                                .state("California")
                                .city("San Francisco")
                                .zipCode("94105")
                                .street("123 Main St")
                                .build()
                        ).build(),
                Customer.builder()
                        .id(2L)
                        .dni("987654321")
                        .fullName("Jane Smith")
                        .phone("555-1234-2")
                        .address(Address.builder()
                                .country("United Kingdom")
                                .state("England")
                                .city("London")
                                .zipCode("EC1A 1BB")
                                .street("456 High St")
                                .build()
                        ).build()
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
    public void createCustomerCreateShouldBeSuccessful() {

        var createdCustomerId = 1L;

        var dto = CreateCustomerDto.builder()
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddressDto)
                .build();

        var mappedCustomer = Customer.builder()
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress)
                .build();

        var newCustomer = Customer.builder()
                .id(createdCustomerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress)
                .build();

        when(customerMapper.createDtoToEntity(dto)).thenReturn(mappedCustomer);
        when(customerRepository.saveAndFlush(mappedCustomer)).thenReturn(newCustomer);

        var resultCustomerId = customerService.createCustomer(dto);
        assertEquals(createdCustomerId, resultCustomerId);
    }

    @Test
    public void updateCustomerWhenCustomerExistsUpdateShouldBeSuccessful() {

        var customerId = 1L;

        var updatedAddressDto = AddressDto.builder()
                .country("US")
                .state("Colorado")
                .city("Denver")
                .zipCode("80014")
                .street("Colorado's Gateway")
                .build();

        var dto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456780")
                .fullName("John Doe Smith")
                .phone("555-1234-9")
                .address(updatedAddressDto)
                .build();

        var foundCustomer = Customer.builder()
                .id(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(foundCustomer));
        customerService.updateCustomer(dto);

        verify(customerRepository, times(1)).saveAndFlush(foundCustomer);

        var customerArgCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).saveAndFlush(customerArgCaptor.capture());
        var updatedCustomer = customerArgCaptor.getValue();
        assertNotNull(updatedCustomer);

        assertEquals(customerId, updatedCustomer.getId());

        assertEquals(dto.getDni(), updatedCustomer.getDni());
        assertEquals(dto.getDni(), updatedCustomer.getDni());
        assertEquals(dto.getDni(), updatedCustomer.getDni());
        assertEquals(dto.getAddress().getCountry(), updatedCustomer.getAddress().getCountry());
        assertEquals(dto.getAddress().getState(), updatedCustomer.getAddress().getState());
        assertEquals(dto.getAddress().getCity(), updatedCustomer.getAddress().getCity());
        assertEquals(dto.getAddress().getZipCode(), updatedCustomer.getAddress().getZipCode());
        assertEquals(dto.getAddress().getStreet(), updatedCustomer.getAddress().getStreet());
    }

    @Test
    public void updateCustomerWhenCustomerDoNotExistsShouldThrowNotFoundException() {

        var customerId = 1L;

        var updatedAddressDto = AddressDto.builder()
                .country("US")
                .state("Colorado")
                .city("Denver")
                .zipCode("80014")
                .street("Colorado's Gateway")
                .build();

        var dto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456780")
                .fullName("John Doe Smith")
                .phone("555-1234-9")
                .address(updatedAddressDto)
                .build();

        when(customerRepository.findById(customerId)).thenThrow(new NotFoundException("Customer Not Found"));
        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(dto));
    }

    @Test
    public void deleteCustomerWhenCustomerExistsDeleteShouldBeSuccessful() {

        var customerId = 1L;

        var foundCustomer = Customer.builder()
                .id(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(foundCustomer));
        doNothing().when(customerRepository).delete(foundCustomer);
        customerService.deleteCustomer(customerId);
        verify(customerRepository, times(1)).delete(foundCustomer);
    }

    @Test
    public void deleteCustomerWhenCustomerDoNotExistsShouldThrowNotFoundException() {
        var customerId = 1L;
        when(customerRepository.findById(customerId)).thenThrow(new NotFoundException("Customer Not Found"));
        assertThrows(NotFoundException.class, () -> customerService.deleteCustomer(customerId));
    }

    @Test
    public void getCustomerWhenCustomerExistsGetShouldBeSuccessful() {

        var customerId = 1L;

        var foundCustomer = Customer.builder()
                .id(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress)
                .build();

        var customerDto = CustomerDetailsDto.builder()
                .id(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddressDto)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(foundCustomer));
        when(customerMapper.entityToDetailsDto(foundCustomer)).thenReturn(customerDto);

        var resultDto = customerService.getCustomerById(customerId);
        assertEquals(customerDto, resultDto);
    }

    @Test
    public void getCustomerWhenCustomerDoNotExistsShouldThrowNotFoundException() {
        var customerId = 1L;
        when(customerRepository.findById(customerId)).thenThrow(new NotFoundException("Customer Not Found"));
        assertThrows(NotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    public void getCustomersShouldReturnList() {

        var expectedResult = customers.stream()
                .map(customerMapper::entityToDto)
                .toList();

        var request = GetCustomersRequest.builder().build();
        Specification<Customer> mockSpec = any();
        when(customerRepository.findAll(mockSpec)).thenReturn(customers);
        var result = customerService.getCustomers(request);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertThat(result).hasSameElementsAs(expectedResult);
    }

    @Test
    public void getSuppliersPageShouldReturnPagedList() {

        var totalSuppliersInDb = 4;

        var totalPages = 2;

        var expectedItems = customers.stream()
                .map(customerMapper::entityToDto)
                .toList();


        var request = GetCustomersRequest.builder()
                .pageNumber(1)
                .pageSize(2)
                .build();

        Specification<Customer> mockSpec = any();
        PageRequest mockPageRequest = any();
        Page<Customer> mockPage = new PageImpl<>(
                customers,
                Pageable.ofSize(2),
                totalSuppliersInDb
        );

        when(customerRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = customerService.getCustomersPaged(request);
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
