package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.repositories.*;
import com.jcanseco.inventoryapi.specifications.PurchaseSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PurchaseRepositoryTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Sql("/multiple-purchases.sql")
    @Test
    public void createPurchaseShouldGenerateId() {

        var supplier = supplierRepository.findById(1L).orElseThrow();
        var productsWithQuantities = new HashMap<Long, Long>() {{
           put(1L, 10L);
           put(2L, 10L);
        }};

        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var items = products.stream().map(p -> PurchaseItem
                .builder()
                .product(p)
                .productId(p.getId())
                .productName(p.getName())
                .productUnit(p.getUnit().getName())
                .price(p.getPurchasePrice())
                .quantity(productsWithQuantities.get(p.getId()))
                .total(p.getPurchasePrice().multiply(BigDecimal.valueOf(productsWithQuantities.get(p.getId()))))
                .build())
                .toList();

        var purchaseTotal = items.stream()
                .map(PurchaseItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var purchase = purchaseRepository.saveAndFlush(
                Purchase.builder()
                        .supplier(supplier)
                        .items(items)
                        .total(purchaseTotal)
                        .build()
        );

        assertNotNull(purchase);
        assertTrue(purchase.getId() > 0);
        assertNotNull(purchase.getCreatedAt());
        assertFalse(purchase.isArrived());
        assertNull(purchase.getArrivedAt());

        var foundPurchase = purchaseRepository.findById(purchase.getId());
        assertTrue(foundPurchase.isPresent());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesByCreatedBetweenSpecificationShouldReturnList() {
        var startDate = LocalDateTime.of(2023, Month.MAY, 1, 0,0);
        var endDate = LocalDateTime.of(2023, Month.MAY, 28, 0, 0);
        var specification = PurchaseSpecifications.byCreatedBetween(startDate, endDate);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(4, purchases.size());
    }


    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesByArrivedBetweenSpecificationShouldReturnList() {
        var startDate = LocalDateTime.of(2023, Month.JUNE, 5, 0,0);
        var endDate = LocalDateTime.of(2023, Month.JUNE, 19, 0, 0);
        var specification = PurchaseSpecifications.arrivedBetween(startDate, endDate);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(3, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesBySupplierSpecificationShouldReturnList() {
        var supplier = supplierRepository.findById(2L).orElseThrow();
        var specification = PurchaseSpecifications.bySupplier(supplier);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(5, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesByIsArrivedSpecificationShouldReturnList() {
        var arrivedPurchases = purchaseRepository.findAll(
                PurchaseSpecifications.isArrived()
        );
        assertNotNull(arrivedPurchases);
        assertEquals(5, arrivedPurchases.size());
    }
}
