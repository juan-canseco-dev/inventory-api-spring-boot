package com.jcanseco.inventoryapi.suppliers.api;

import com.jcanseco.inventoryapi.suppliers.dto.CreateSupplierDto;
import com.jcanseco.inventoryapi.suppliers.dto.GetSuppliersRequest;
import com.jcanseco.inventoryapi.suppliers.dto.SupplierDetailsDto;
import com.jcanseco.inventoryapi.suppliers.dto.UpdateSupplierDto;
import com.jcanseco.inventoryapi.suppliers.usecases.create.CreateSupplierUseCase;
import com.jcanseco.inventoryapi.suppliers.usecases.delete.DeleteSupplierUseCase;
import com.jcanseco.inventoryapi.suppliers.usecases.getall.GetSuppliersUseCase;
import com.jcanseco.inventoryapi.suppliers.usecases.getbyid.GetSupplierByIdUseCase;
import com.jcanseco.inventoryapi.suppliers.usecases.update.UpdateSupplierUseCase;
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
@RequestMapping("api/suppliers")
@RestController
@RequiredArgsConstructor
public class SupplierController {

    private final CreateSupplierUseCase createSupplierUseCase;
    private final UpdateSupplierUseCase updateSupplierUseCase;
    private final DeleteSupplierUseCase deleteSupplierUseCase;
    private final GetSupplierByIdUseCase getSupplierByIdUseCase;
    private final GetSuppliersUseCase getSuppliersUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSupplierDto dto) throws URISyntaxException {
        var supplierId = createSupplierUseCase.execute(dto);
        var location = new URI("/api/suppliers/" + supplierId);
        return ResponseEntity.created(location).body(supplierId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.Update))")
    @PutMapping("{supplierId}")
    public ResponseEntity<Void> update(@PathVariable Long supplierId, @RequestBody @Valid UpdateSupplierDto dto) {
        if (!dto.getSupplierId().equals(supplierId)) {
            return ResponseEntity.badRequest().build();
        }
        updateSupplierUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.Delete))")
    @DeleteMapping("{supplierId}")
    public ResponseEntity<Void> delete(@PathVariable Long supplierId) {
        deleteSupplierUseCase.execute(supplierId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.View))")
    @GetMapping("{supplierId}")
    public ResponseEntity<SupplierDetailsDto> getById(@PathVariable Long supplierId) {
        var response = getSupplierByIdUseCase.execute(supplierId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Suppliers, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetSuppliersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getSuppliersUseCase.execute(request));
        }
        return ResponseEntity.ok(getSuppliersUseCase.executePaged(request));
    }
}
