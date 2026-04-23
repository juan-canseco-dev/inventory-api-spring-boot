package com.jcanseco.inventoryapi.purchases.usecases.delete;

import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletePurchaseUseCase {

    private final PurchaseRepository purchaseRepository;

    @Transactional
    public void execute(Long purchaseId) {
        var purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found.", purchaseId)));

        if (purchase.isArrived()) {
            throw new DomainException(String.format("Cannot delete the purchase with ID %d because it has already arrived.", purchaseId));
        }

        purchaseRepository.delete(purchase);
    }
}

