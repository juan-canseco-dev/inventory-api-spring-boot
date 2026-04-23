package com.jcanseco.inventoryapi.bootstrap.data.purchases;

import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications;
import com.jcanseco.inventoryapi.purchases.dto.CreatePurchaseDto;
import com.jcanseco.inventoryapi.purchases.dto.ReceivePurchaseDto;
import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
import com.jcanseco.inventoryapi.purchases.usecases.create.CreatePurchaseUseCase;
import com.jcanseco.inventoryapi.purchases.usecases.receive.ReceivePurchaseUseCase;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;





@Profile("!test")
@Slf4j
@Order(9)
@Component
@RequiredArgsConstructor
public class PurchaseDataInitializer implements ApplicationRunner {

    private static final int NUM_OF_PURCHASES_BY_SUPPLIER = 10;

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final CreatePurchaseUseCase createPurchaseUseCase;
    private final ReceivePurchaseUseCase receivePurchaseUseCase;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting purchase data initialization.");

        long existingPurchases = purchaseRepository.count();
        if (existingPurchases > 0) {
            log.info("Skipping purchase seeding. Found {} existing purchases.", existingPurchases);
            return;
        }

        var suppliers = supplierRepository.findAll();
        log.info("Found {} suppliers for purchase generation.", suppliers.size());

        int totalPurchasesCreated = 0;

        for (var supplier : suppliers) {
            var productsBySupplier = productRepository.findAll(bySupplierSpec(supplier));

            if (productsBySupplier.isEmpty()) {
                log.warn("Skipping supplier with id {} because no products were found.", supplier.getId());
                continue;
            }

            log.info(
                    "Generating {} purchases for supplier id {} with {} products.",
                    NUM_OF_PURCHASES_BY_SUPPLIER,
                    supplier.getId(),
                    productsBySupplier.size()
            );

            long initialQuantity = 10L;

            for (int i = 0; i < NUM_OF_PURCHASES_BY_SUPPLIER; i++) {
                var productsWithQuantitiesMap = createQuantityMapFromProduct(productsBySupplier, initialQuantity);

                var createPurchaseDto = CreatePurchaseDto.builder()
                        .supplierId(supplier.getId())
                        .productsWithQuantities(productsWithQuantitiesMap)
                        .build();

                Long purchaseId = createPurchaseUseCase.execute(createPurchaseDto);
                String receivePurchaseComments = "Received the purchase thank you :)";
                log.debug(
                        "Created purchase with id {} for supplier id {} using quantity {} for {} products.",
                        purchaseId,
                        supplier.getId(),
                        initialQuantity,
                        productsWithQuantitiesMap.size()
                );

                var receivePurchaseDto = ReceivePurchaseDto.builder()
                                .purchaseId(purchaseId)
                        .comment(receivePurchaseComments)
                        .build();

                receivePurchaseUseCase.execute(receivePurchaseDto);
                log.debug(
                        "Received purchase with id {} for supplier id {}. Stock updated successfully.",
                        purchaseId,
                        supplier.getId()
                );

                totalPurchasesCreated++;
                initialQuantity += 10L;
            }
        }

        log.info("Purchase data initialization completed successfully. Total purchases created and received: {}.", totalPurchasesCreated);
    }

    private Specification<Product> bySupplierSpec(Supplier supplier) {
        return ProductSpecifications.bySupplier(supplier);
    }

    private HashMap<Long, Long> createQuantityMapFromProduct(List<Product> products, Long productQuantity) {
        return products.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        product -> productQuantity,
                        Long::sum,
                        HashMap::new
                ));
    }
}





