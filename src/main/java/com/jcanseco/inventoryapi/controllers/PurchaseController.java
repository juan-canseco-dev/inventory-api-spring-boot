 package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.purchases.CreatePurchaseDto;
import com.jcanseco.inventoryapi.dtos.purchases.GetPurchasesRequest;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseDetailsDto;
import com.jcanseco.inventoryapi.dtos.purchases.UpdatePurchaseDto;
import com.jcanseco.inventoryapi.services.PurchaseService;
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
@RequestMapping("api/purchases")
@RestController
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreatePurchaseDto dto) throws URISyntaxException {
        var purchaseId = purchaseService.createPurchase(dto);
        var location = new URI("/api/purchases/" + purchaseId);
        return ResponseEntity.created(location).body(purchaseId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Update))")
    @PutMapping("{purchaseId}")
    public ResponseEntity<Void> update(@PathVariable Long purchaseId, @RequestBody @Valid UpdatePurchaseDto dto) {
        if (!dto.getPurchaseId().equals(purchaseId)) {
            return ResponseEntity.badRequest().build();
        }
        purchaseService.updatePurchase(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Receive))")
    @PutMapping("{purchaseId}/receive")
    public ResponseEntity<Void> receive(@PathVariable Long purchaseId) {
        purchaseService.receivePurchase(purchaseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Delete))")
    @DeleteMapping("{purchaseId}")
    public ResponseEntity<Void> delete(@PathVariable Long purchaseId) {
        purchaseService.deletePurchase(purchaseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.View))")
    @GetMapping("{purchaseId}")
    public ResponseEntity<PurchaseDetailsDto> getById(@PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(purchaseId));
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetPurchasesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(purchaseService.getPurchases(request));
        }
        return ResponseEntity.ok(purchaseService.getPurchasesPage(request));
    }
}
