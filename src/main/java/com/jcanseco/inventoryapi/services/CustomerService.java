package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.*;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CustomerMapper;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import com.jcanseco.inventoryapi.specifications.CustomerSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final String NOT_FOUND_MESSAGE = "Customer with the Id {%d} was not found.";

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final IndexUtility indexUtility;

    public Long createCustomer(CreateCustomerDto dto) {
        var customer = customerMapper.createDtoToEntity(dto);
        var newCustomer = customerRepository.saveAndFlush(customer);
        return newCustomer.getId();
    }

    public void updateCustomer(UpdateCustomerDto dto) {

        var customer = customerRepository
                .findById(dto.getCustomerId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, dto.getCustomerId())));

        customer.setDni(dto.getDni());
        customer.setPhone(dto.getPhone());
        customer.setFullName(dto.getFullName());
        customer.setAddress(customerMapper.dtoToAddress(dto.getAddress()));

        customerRepository.saveAndFlush(customer);
    }


    public void deleteCustomer(Long customerId) {
        var customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, customerId)));

        customerRepository.delete(customer);
    }

    public CustomerDetailsDto getCustomerById(Long customerId) {
        return customerRepository
                .findById(customerId)
                .map(customerMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, customerId)));
    }


    private Specification<Customer> composeSpecification(GetCustomersRequest request) {

        Specification<Customer> specification = Specification.where(null);
        if (StringUtils.hasText(request.getDni())) {
            specification = specification.and(CustomerSpecifications.byDniLike(request.getDni()));
        }
        if (StringUtils.hasText(request.getPhone())) {
            specification = specification.and(CustomerSpecifications.byPhoneLike(request.getPhone()));
        }
        if (StringUtils.hasText(request.getFullName())) {
            specification = specification.and(CustomerSpecifications.byFullNameLike(request.getFullName()));
        }

        var isAscendingOrder = indexUtility.isAscendingOrder(request.getSortOrder());
        var orderByField = !StringUtils.hasText(request.getOrderBy())? "id" : request.getOrderBy();

        return CustomerSpecifications.orderBy(
                specification,
                orderByField,
                isAscendingOrder
        );
    }

    public List<CustomerDto> getCustomers(GetCustomersRequest request) {

        var specification = composeSpecification(request);

        return customerRepository
                .findAll(specification)
                .stream()
                .map(customerMapper::entityToDto)
                .toList();
    }

    public PagedList<CustomerDto> getCustomersPaged(GetCustomersRequest request) {

        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);

        var page = customerRepository.findAll(specification, pageRequest);

        return customerMapper.pageToPagedList(page);
    }
}