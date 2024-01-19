package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.repositories.*;
import com.jcanseco.inventoryapi.specifications.PurchaseSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
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
        assertNotNull(purchase.getOrderedAt());
        assertFalse(purchase.isArrived());
        assertNull(purchase.getArrivedAt());

        var foundPurchase = purchaseRepository.findById(purchase.getId());
        assertTrue(foundPurchase.isPresent());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesByOrderedBetweenSpecificationShouldReturnList() {
        var startDate = LocalDateTime.of(2023, Month.MAY, 1, 0,0);
        var endDate = LocalDateTime.of(2023, Month.MAY, 28, 0, 0);
        var specification = PurchaseSpecifications.byOrderedBetween(startDate, endDate);
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

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByIdAscFirstPurchaseIdMustBeOne() {
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByIdAsc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(1L, firstPurchase.getId());
        assertEquals(10, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByIdDescFirstPurchaseIdMustBeTen() {
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByIdDesc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(10L, firstPurchase.getId());
        assertEquals(10, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByTotalAscFirstPurchaseTotalMustBeExpected() {
        var firstTotal = new BigDecimal("1150.00");
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByTotalAsc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(firstTotal, firstPurchase.getTotal());
        assertEquals(10, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByTotalDescFirstPurchaseTotalMustBeExpected() {
        var firstTotal = new BigDecimal("13000.00");
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByTotalDesc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(firstTotal, firstPurchase.getTotal());
        assertEquals(10, purchases.size());
    }


    // Suppliers

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderBySupplierAscFirstPurchaseSupplierMustBeExpected() {
        var firstSupplierId = 1L;
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderBySupplierAsc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(firstSupplierId, firstPurchase.getSupplier().getId());
        assertEquals(10, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderBySupplierDescFirstPurchaseSupplierMustBeExpected() {
        var firstSupplierId = 2L;
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderBySupplierDesc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(firstSupplierId, firstPurchase.getSupplier().getId());
        assertEquals(10, purchases.size());
    }

    // Ordered At

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByOrderedAtAscFirstPurchaseOrderedAtMustBeExpected() {
        var expectedDate = LocalDateTime.of(2023, Month.MAY, 1, 0,0);
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByOrderedAtAsc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(expectedDate, firstPurchase.getOrderedAt());
        assertEquals(10, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByOrderedAtDescFirstPurchaseOrderedAtMustBeExpected() {
        var expectedDate = LocalDateTime.of(2023, Month.JULY, 3, 0,0);
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByOrderedAtDesc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(expectedDate, firstPurchase.getOrderedAt());
        assertEquals(10, purchases.size());
    }

    // Arrived At
    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByArrivedAtAscFirstPurchaseArrivedAtMustBeExpected() {
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByArrivedAtAsc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertNull(firstPurchase.getArrivedAt());
        assertEquals(10, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByArrivedAtDescFirstPurchaseArrivedAtMustBeExpected() {
        var expectedDate = LocalDateTime.of(2023, Month.JULY, 3, 0,0);
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByArrivedAtDesc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertEquals(expectedDate, firstPurchase.getArrivedAt());
        assertEquals(10, purchases.size());
    }

    // Arrived
    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByArrivedAscFirstPurchaseMustNotArrived() {
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByArrivedAsc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertFalse(firstPurchase.isArrived());
        assertEquals(10, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesOrderByArrivedDescFirstPurchaseMustBeArrived() {
        var purchases = purchaseRepository.findAll(
                PurchaseSpecifications.orderByArrivedDesc(Specification.where(null))
        );
        assertNotNull(purchases);
        var firstPurchase = purchases.get(0);
        assertTrue(firstPurchase.isArrived());
        assertEquals(10, purchases.size());
    }

}
