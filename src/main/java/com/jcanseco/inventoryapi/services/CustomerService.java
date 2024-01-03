package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.*;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CustomerMapper;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Long createCustomer(CreateCustomerDto dto) {
        var customer = customerMapper.createDtoToEntity(dto);
        var newCustomer = customerRepository.saveAndFlush(customer);
        return newCustomer.getId();
    }

    public void updateCustomer(UpdateCustomerDto dto) {

        var customer = customerRepository
                .findById(dto.getCustomerId())
                .orElseThrow(() -> new NotFoundException(String.format("Customer with the Id {%d} was not found.", dto.getCustomerId())));

        customer.setDni(dto.getDni());
        customer.setPhone(dto.getPhone());
        customer.setFullName(dto.getFullName());
        customer.setAddress(customerMapper.dtoToAddress(dto.getAddress()));

        customerRepository.saveAndFlush(customer);
    }


    public void deleteCustomer(Long customerId) {
        var customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(String.format("Customer with the Id {%d} was not found.", customerId)));

        customerRepository.delete(customer);
    }

    public CustomerDetailsDto getCustomerById(Long customerId) {
        return customerRepository
                .findById(customerId)
                .map(customerMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Customer with the Id {%d} was not found.", customerId)));
    }


    private boolean sortOrderIsAscending(GetCustomersRequest request) {
        if (request.getSortOrder() == null) {
            return true;
        }
        return request.getSortOrder().equals("asc") || request.getSortOrder().equals("ascending");
    }

    private String getOrderBy(GetCustomersRequest request) {
        if (request.getOrderBy() == null) {
            return "fullName";
        }
        return request.getOrderBy();
    }

    private Sort getSortOrder(GetCustomersRequest request) {
        var ascending = sortOrderIsAscending(request);
        var orderBy = getOrderBy(request);
        if (ascending) {
            return Sort.by(orderBy).ascending();
        }
        return Sort.by(orderBy).descending();
    }

    public List<CustomerDto> getCustomers(GetCustomersRequest request) {

        var sort = getSortOrder(request);

        var specification = CustomerRepository.Specs.byDniOrPhoneOrFullName(
                request.getDni(),
                request.getPhone(),
                request.getFullName()
        );

        if (specification == null) {
            return customerRepository.findAll(sort)
                    .stream()
                    .map(customerMapper::entityToDto)
                    .toList();
        }

        return customerRepository
                .findAll(specification, sort)
                .stream()
                .map(customerMapper::entityToDto)
                .toList();
    }

    public PagedList<CustomerDto> getCustomersPaged(GetCustomersRequest request) {

        var pageNumber = request.getPageNumber() > 0 ? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();

        var specification = CustomerRepository.Specs.byDniOrPhoneOrFullName(
                request.getDni(),
                request.getPhone(),
                request.getFullName()
        );

        var sort = getSortOrder(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        var page = specification == null ? customerRepository.findAll(pageRequest) : customerRepository.findAll(specification, pageRequest);

        var items = page.get().map(customerMapper::entityToDto).toList();
        var totalPages = page.getTotalPages();
        var totalElements = page.getTotalElements();

        return new PagedList<>(
                items,
                request.getPageNumber(),
                pageSize,
                totalPages,
                totalElements
        );
    }
}