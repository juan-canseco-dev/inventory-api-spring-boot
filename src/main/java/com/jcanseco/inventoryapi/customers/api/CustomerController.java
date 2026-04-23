package com.jcanseco.inventoryapi.customers.api;

import com.jcanseco.inventoryapi.customers.dto.CreateCustomerDto;
import com.jcanseco.inventoryapi.customers.dto.CustomerDetailsDto;
import com.jcanseco.inventoryapi.customers.dto.GetCustomersRequest;
import com.jcanseco.inventoryapi.customers.dto.UpdateCustomerDto;
import com.jcanseco.inventoryapi.customers.usecases.create.CreateCustomerUseCase;
import com.jcanseco.inventoryapi.customers.usecases.delete.DeleteCustomerUseCase;
import com.jcanseco.inventoryapi.customers.usecases.getall.GetCustomersUseCase;
import com.jcanseco.inventoryapi.customers.usecases.getbyid.GetCustomerByIdUseCase;
import com.jcanseco.inventoryapi.customers.usecases.update.UpdateCustomerUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestControllerAdvice
@RequestMapping("api/customers")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final GetCustomersUseCase getCustomersUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Customers, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateCustomerDto dto) throws URISyntaxException {
        var customerId = createCustomerUseCase.execute(dto);
        var location = new URI("/api/customers/" + customerId);
        return ResponseEntity.created(location).body(customerId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Customers, @Action.Update))")
    @PutMapping("{customerId}")
    public ResponseEntity<Void> update(@PathVariable Long customerId, @RequestBody @Valid UpdateCustomerDto dto) {
        if (!dto.getCustomerId().equals(customerId)) {
            return ResponseEntity.badRequest().build();
        }
        updateCustomerUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Customers, @Action.Delete))")
    @DeleteMapping("{customerId}")
    public ResponseEntity<Void> delete(@PathVariable Long customerId) {
        deleteCustomerUseCase.execute(customerId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Customers, @Action.View))")
    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDetailsDto> getById(@PathVariable Long customerId) {
        var response = getCustomerByIdUseCase.execute(customerId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Customers, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetCustomersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getCustomersUseCase.execute(request));
        }
        return ResponseEntity.ok(getCustomersUseCase.executePaged(request));
    }
}
