package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.GetCustomersRequest;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import com.jcanseco.inventoryapi.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@AllArgsConstructor
@RestControllerAdvice
@RequestMapping("api/customers")
@RestController
public class CustomersController {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody @Valid CreateCustomerDto dto) {
        return ResponseEntity.ok(service.createCustomer(dto));
    }

    @PutMapping("{customerId}")
    public ResponseEntity<CustomerDto> update(@PathVariable Long customerId, @RequestBody @Valid UpdateCustomerDto dto) {
        if (!dto.getCustomerId().equals(customerId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.updateCustomer(dto));
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<?> delete(@PathVariable Long customerId) {
        service.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDto> getById(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getCustomerById(customerId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetCustomersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getCustomers(request);
            return ResponseEntity.ok(response);
        }
        var response = service.getCustomersPaged(request);
        return  ResponseEntity.ok(response);
    }
}
