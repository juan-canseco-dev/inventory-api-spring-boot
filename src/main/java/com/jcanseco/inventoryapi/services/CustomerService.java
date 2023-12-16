package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.GetCustomersRequest;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CreateCustomerDto dto);
    CustomerDto updateCustomer(UpdateCustomerDto dto);
    void deleteCustomer(Long customerId);
    CustomerDto getCustomerById(Long customerId);
    List<CustomerDto> getCustomers(GetCustomersRequest request);
    PagedList<CustomerDto> getCustomersPaged(GetCustomersRequest request);
}
