package com.jcanseco.inventoryapi.customers.usecases.create;

import com.jcanseco.inventoryapi.customers.dto.CreateCustomerDto;
import com.jcanseco.inventoryapi.customers.mapping.CustomerMapper;
import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Long execute(CreateCustomerDto dto) {
        var customer = customerMapper.createDtoToEntity(dto);
        var newCustomer = customerRepository.saveAndFlush(customer);
        return newCustomer.getId();
    }
}

