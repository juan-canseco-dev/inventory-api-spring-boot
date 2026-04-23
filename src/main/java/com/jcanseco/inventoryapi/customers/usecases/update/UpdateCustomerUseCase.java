package com.jcanseco.inventoryapi.customers.usecases.update;

import com.jcanseco.inventoryapi.customers.dto.UpdateCustomerDto;
import com.jcanseco.inventoryapi.customers.mapping.CustomerMapper;
import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private static final String NOT_FOUND_MESSAGE = "Customer with the Id {%d} was not found.";

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public void execute(UpdateCustomerDto dto) {
        var customer = customerRepository
                .findById(dto.getCustomerId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, dto.getCustomerId())));

        customer.setDni(dto.getDni());
        customer.setPhone(dto.getPhone());
        customer.setFullName(dto.getFullName());
        customer.setAddress(customerMapper.dtoToAddress(dto.getAddress()));

        customerRepository.saveAndFlush(customer);
    }
}

