package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.customers.*;
import com.jcanseco.inventoryapi.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@RequestMapping("api/customers")
@RestControllerAdvice
@RestController
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerService customerService;
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateCustomerDto dto) throws URISyntaxException {
        var customerId = customerService.createCustomer(dto);
        var location = new URI("/api/customers/" + customerId);
        return ResponseEntity.created(location).body(customerId);
    }

    @PutMapping("{customerId}")
    public ResponseEntity<?> update(@PathVariable Long customerId, @RequestBody @Valid UpdateCustomerDto dto) {
        if (!dto.getCustomerId().equals(customerId)) {
            return ResponseEntity.badRequest().build();
        }
        customerService.updateCustomer(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<?> delete(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDetailsDto> getById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetCustomersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = customerService.getCustomers(request);
            return ResponseEntity.ok(response);
        }
        var response = customerService.getCustomersPaged(request);
        return  ResponseEntity.ok(response);
    }
}
