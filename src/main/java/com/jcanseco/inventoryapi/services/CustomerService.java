package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.*;
import java.util.List;

public interface CustomerService {
    Long createCustomer(CreateCustomerDto dto);
    void updateCustomer(UpdateCustomerDto dto);
    void deleteCustomer(Long customerId);
    CustomerDetailsDto getCustomerById(Long customerId);
    List<CustomerDto> getCustomers(GetCustomersRequest request);
    PagedList<CustomerDto> getCustomersPaged(GetCustomersRequest request);
}
