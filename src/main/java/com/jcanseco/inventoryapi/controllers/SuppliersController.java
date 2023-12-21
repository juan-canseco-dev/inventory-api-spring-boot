package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.suppliers.*;
import com.jcanseco.inventoryapi.services.SupplierService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@AllArgsConstructor
@RestControllerAdvice
@RequestMapping("api/suppliers")
@RestController
public class SuppliersController {

    private final SupplierService service;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSupplierDto dto) throws URISyntaxException {
        var supplierId = service.createSupplier(dto);
        var location = new URI("/api/suppliers/" + supplierId);
        return ResponseEntity.created(location).body(supplierId);
    }

    @PutMapping("{supplierId}")
    public ResponseEntity<?> update(@PathVariable Long supplierId, @RequestBody @Valid UpdateSupplierDto dto) {
        if (!dto.getSupplierId().equals(supplierId)) {
            return ResponseEntity.badRequest().build();
        }
        service.updateSupplier(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{supplierId}")
    public ResponseEntity<?> delete(@PathVariable Long supplierId) {
        service.deleteSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{supplierId}")
    public ResponseEntity<SupplierDetailsDto> getById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getSupplierById(supplierId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetSuppliersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getSuppliers(request);
            return ResponseEntity.ok(response);
        }
        var response = service.getSuppliersPaged(request);
        return ResponseEntity.ok(response);
    }

}
