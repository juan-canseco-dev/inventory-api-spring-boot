package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.GetSuppliersRequest;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;
import com.jcanseco.inventoryapi.services.SupplierService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@AllArgsConstructor
@RestControllerAdvice
@RequestMapping("api/suppliers")
@RestController
public class SuppliersController {

    private final SupplierService service;

    @PostMapping
    public ResponseEntity<SupplierDto> create(@RequestBody @Valid CreateSupplierDto dto) {
        return ResponseEntity.ok(service.createSupplier(dto));
    }

    @PutMapping("{supplierId}")
    public ResponseEntity<SupplierDto> update(@PathVariable Long supplierId, @RequestBody @Valid UpdateSupplierDto dto) {
        if (!dto.getSupplierId().equals(supplierId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.updateSupplier(dto));
    }

    @DeleteMapping("{supplierId}")
    public ResponseEntity<?> delete(@PathVariable Long supplierId) {
        service.deleteSupplier(supplierId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{supplierId}")
    public ResponseEntity<SupplierDto> getById(@PathVariable Long supplierId) {
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
