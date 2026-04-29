package com.jcanseco.inventoryapi.bootstrap.data.purchases;

import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications;
import com.jcanseco.inventoryapi.inventory.stock.persistence.StockRepository;
import com.jcanseco.inventoryapi.purchases.domain.Purchase;
import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;

import java.time.LocalDateTime;
import java.time.Month;
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

import static com.jcanseco.inventoryapi.inventory.stock.persistence.StockSpecifications.byProductIds;

@Profile("!test")
@Slf4j
@Order(9)
@Component
@RequiredArgsConstructor
public class PurchaseDataInitializer implements ApplicationRunner {

    private static final int NUM_OF_PURCHASES_BY_SUPPLIER = 10;

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final PurchaseRepository purchaseRepository;

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
        LocalDateTime startDate = LocalDateTime.of(2022, Month.JULY, 1, 0,0);

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

                Long purchaseId = createPurchaseAndMarkAsReceived(supplier.getId(), productsWithQuantitiesMap, startDate);
                startDate = startDate.plusDays(7);

                log.debug(
                        "Created purchase with id {} for supplier id {} using quantity {} for {} products.",
                        purchaseId,
                        supplier.getId(),
                        initialQuantity,
                        productsWithQuantitiesMap.size()
                );


                totalPurchasesCreated++;
                initialQuantity += 10L;
            }
        }

        log.info("Purchase data initialization completed successfully. Total purchases created and received: {}.", totalPurchasesCreated);
    }

    private Long createPurchaseAndMarkAsReceived(Long supplierId, HashMap<Long, Long> productsWithQuantities, LocalDateTime createdAt) {
        var supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new DomainException(String.format("Supplier with the Id : {%d} was not found.", supplierId)));

        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var newPurchase = Purchase.createNew(supplier, products, productsWithQuantities, createdAt);
        newPurchase.markAsArrived("Arrived", createdAt.plusDays(1));

        var savedPurchase = purchaseRepository.saveAndFlush(
                newPurchase
        );

        var stocks = stockRepository.findAll(byProductIds(productsWithQuantities.keySet().stream().toList()));
        stocks.forEach(stock -> stock.addStock(productsWithQuantities.get(stock.getProductId())));

        stockRepository.saveAllAndFlush(stocks);

        return savedPurchase.getId();
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





