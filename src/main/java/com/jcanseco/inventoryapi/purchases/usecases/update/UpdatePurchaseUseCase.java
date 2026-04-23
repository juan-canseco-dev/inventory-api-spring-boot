package com.jcanseco.inventoryapi.purchases.usecases.update;

import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.purchases.dto.UpdatePurchaseDto;
import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePurchaseUseCase {

    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public void execute(UpdatePurchaseDto dto) {
        var purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found.", dto.getPurchaseId())));

        if (purchase.isArrived()) {
            throw new DomainException(String.format("Cannot update the purchase with ID %d because it has already arrived.", dto.getPurchaseId()));
        }

        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());
        purchase.update(products, productsWithQuantities);

        purchaseRepository.saveAndFlush(purchase);
    }
}

