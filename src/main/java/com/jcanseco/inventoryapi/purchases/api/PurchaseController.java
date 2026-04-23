package com.jcanseco.inventoryapi.purchases.api;

import com.jcanseco.inventoryapi.purchases.dto.*;
import com.jcanseco.inventoryapi.purchases.usecases.create.CreatePurchaseUseCase;
import com.jcanseco.inventoryapi.purchases.usecases.delete.DeletePurchaseUseCase;
import com.jcanseco.inventoryapi.purchases.usecases.getall.GetPurchasesUseCase;
import com.jcanseco.inventoryapi.purchases.usecases.getbyid.GetPurchaseByIdUseCase;
import com.jcanseco.inventoryapi.purchases.usecases.receive.ReceivePurchaseUseCase;
import com.jcanseco.inventoryapi.purchases.usecases.update.UpdatePurchaseUseCase;
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
@RequestMapping("api/purchases")
@RestController
@RequiredArgsConstructor
public class PurchaseController {

    private final CreatePurchaseUseCase createPurchaseUseCase;
    private final UpdatePurchaseUseCase updatePurchaseUseCase;
    private final ReceivePurchaseUseCase receivePurchaseUseCase;
    private final DeletePurchaseUseCase deletePurchaseUseCase;
    private final GetPurchaseByIdUseCase getPurchaseByIdUseCase;
    private final GetPurchasesUseCase getPurchasesUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreatePurchaseDto dto) throws URISyntaxException {
        var purchaseId = createPurchaseUseCase.execute(dto);
        var location = new URI("/api/purchases/" + purchaseId);
        return ResponseEntity.created(location).body(purchaseId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Update))")
    @PutMapping("{purchaseId}")
    public ResponseEntity<Void> update(@PathVariable Long purchaseId, @RequestBody @Valid UpdatePurchaseDto dto) {
        if (!dto.getPurchaseId().equals(purchaseId)) {
            return ResponseEntity.badRequest().build();
        }
        updatePurchaseUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Receive))")
    @PutMapping("receive")
    public ResponseEntity<Void> receive(@RequestBody @Valid ReceivePurchaseDto dto) {
        receivePurchaseUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.Delete))")
    @DeleteMapping("{purchaseId}")
    public ResponseEntity<Void> delete(@PathVariable Long purchaseId) {
        deletePurchaseUseCase.execute(purchaseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.View))")
    @GetMapping("{purchaseId}")
    public ResponseEntity<PurchaseDetailsDto> getById(@PathVariable Long purchaseId) {
        var response = getPurchaseByIdUseCase.execute(purchaseId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Purchases, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetPurchasesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getPurchasesUseCase.execute(request));
        }
        return ResponseEntity.ok(getPurchasesUseCase.executePaged(request));
    }
}
