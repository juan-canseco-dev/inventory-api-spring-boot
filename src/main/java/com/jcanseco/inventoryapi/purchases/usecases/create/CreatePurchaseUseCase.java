package com.jcanseco.inventoryapi.purchases.usecases.create;

import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.purchases.dto.CreatePurchaseDto;
import com.jcanseco.inventoryapi.purchases.domain.Purchase;
import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.utils.ClockProvider;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePurchaseUseCase {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;
    private final ClockProvider clockProvider;

    @Transactional
    public Long execute(CreatePurchaseDto dto) {
        var supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new DomainException(String.format("Supplier with the Id : {%d} was not found.", dto.getSupplierId())));
        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var savedPurchase = purchaseRepository.saveAndFlush(
                Purchase.createNew(supplier, products, productsWithQuantities, clockProvider.now())
        );

        return savedPurchase.getId();
    }
}

