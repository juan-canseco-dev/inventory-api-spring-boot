package com.jcanseco.inventoryapi.customers.usecases.delete;

import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCustomerUseCase {

    private static final String NOT_FOUND_MESSAGE = "Customer with the Id {%d} was not found.";

    private final CustomerRepository customerRepository;

    public void execute(Long customerId) {
        var customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, customerId)));

        customerRepository.delete(customer);
    }
}

