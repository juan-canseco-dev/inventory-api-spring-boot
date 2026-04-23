package com.jcanseco.inventoryapi.customers.usecases.getbyid;

import com.jcanseco.inventoryapi.customers.dto.CustomerDetailsDto;
import com.jcanseco.inventoryapi.customers.mapping.CustomerMapper;
import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCustomerByIdUseCase {

    private static final String NOT_FOUND_MESSAGE = "Customer with the Id {%d} was not found.";

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDetailsDto execute(Long customerId) {
        return customerRepository
                .findById(customerId)
                .map(customerMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, customerId)));
    }
}

