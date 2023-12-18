package com.jcanseco.inventoryapi.services.impl;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.GetCustomersRequest;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CustomerMapper;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import com.jcanseco.inventoryapi.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public CustomerDto createCustomer(CreateCustomerDto dto) {
        var customer = mapper.createDtoToEntity(dto);
        var newCustomer = repository.saveAndFlush(customer);
        return  mapper.entityToDto(newCustomer);
    }

    @Override
    public CustomerDto updateCustomer(UpdateCustomerDto dto) {

        var customer = repository
                .findById(dto.getCustomerId())
                .orElseThrow(() -> new NotFoundException(String.format("Customer with the Id {%d} was not found.", dto.getCustomerId())));

        customer.setDni(dto.getDni());
        customer.setPhone(dto.getPhone());
        customer.setFullName(dto.getFullName());
        customer.setAddress(mapper.dtoToAddress(dto.getAddress()));

        var updatedSupplier = repository.saveAndFlush(customer);

        return mapper.entityToDto(updatedSupplier);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        var customer = repository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(String.format("Customer with the Id {%d} was not found.", customerId)));

        repository.delete(customer);
    }

    @Override
    public CustomerDto getCustomerById(Long customerId) {
        return repository
                .findById(customerId)
                .map(mapper::entityToDto)
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

    @Override
    public List<CustomerDto> getCustomers(GetCustomersRequest request) {

        var sort = getSortOrder(request);

        var specification = CustomerRepository.Specs.byDniOrPhoneOrFullName(
                request.getDni(),
                request.getPhone(),
                request.getFullName()
        );

        if (specification == null) {
            return repository.findAll(sort)
                    .stream()
                    .map(mapper::entityToDto)
                    .toList();
        }

        return repository
                .findAll(specification, sort)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    public PagedList<CustomerDto> getCustomersPaged(GetCustomersRequest request) {

        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();

        var specification = CustomerRepository.Specs.byDniOrPhoneOrFullName(
                request.getDni(),
                request.getPhone(),
                request.getFullName()
        );

        var sort = getSortOrder(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        var page = specification == null? repository.findAll(pageRequest) : repository.findAll(specification, pageRequest);

        var items = page.get().map(mapper::entityToDto).toList();
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
