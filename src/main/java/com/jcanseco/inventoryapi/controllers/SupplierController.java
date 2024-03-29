package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.suppliers.*;
import com.jcanseco.inventoryapi.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@RestControllerAdvice
@RequestMapping("api/suppliers")
@RestController
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSupplierDto dto) throws URISyntaxException {
        var supplierId = supplierService.createSupplier(dto);
        var location = new URI("/api/suppliers/" + supplierId);
        return ResponseEntity.created(location).body(supplierId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.Update))")
    @PutMapping("{supplierId}")
    public ResponseEntity<Void> update(@PathVariable Long supplierId, @RequestBody @Valid UpdateSupplierDto dto) {
        if (!dto.getSupplierId().equals(supplierId)) {
            return ResponseEntity.badRequest().build();
        }
        supplierService.updateSupplier(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.Delete))")
    @DeleteMapping("{supplierId}")
    public ResponseEntity<Void> delete(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.View))")
    @GetMapping("{supplierId}")
    public ResponseEntity<SupplierDetailsDto> getById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierById(supplierId));
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetSuppliersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = supplierService.getSuppliers(request);
            return ResponseEntity.ok(response);
        }
        var response = supplierService.getSuppliersPaged(request);
        return ResponseEntity.ok(response);
    }

}
