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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PurchaseRepositoryTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitOfMeasurementRepository unitRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;


    @Test
    public void createPurchaseShouldGenerateId() {

        var unit = unitRepository.saveAndFlush(
                UnitOfMeasurement.builder()
                        .name("Box")
                        .build()
        );

        var category = categoryRepository.saveAndFlush(
                Category.builder()
                        .name("Electronics")
                        .build()
        );

        var supplier = supplierRepository.saveAndFlush(

                Supplier.builder()
                        .companyName("Pika Games")
                        .contactName("Perla Lopez")
                        .contactPhone( "555-1234-1")
                        .address(Address.builder()
                                .country("Mexico")
                                .state("Sonora")
                                .city("Hermosillo")
                                .zipCode("83200")
                                .street("Center")
                                .build())
                        .build()
        );

        var product1 = productRepository.saveAndFlush(
                Product.builder()
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Halo 3")
                        .stock(Stock.builder().quantity(0L).build())
                        .purchasePrice(new BigDecimal("5.99"))
                        .salePrice(new BigDecimal("9.99"))
                        .build()
        );

        var product2 = productRepository.saveAndFlush(
                Product.builder()
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Halo Infinite")
                        .stock(Stock.builder().quantity(0L).build())
                        .purchasePrice(new BigDecimal("39.99"))
                        .salePrice(new BigDecimal("59.99"))
                        .build()
        );

        var item1Quantity = 10L;
        var item1 = PurchaseItem.builder()
                .product(product1)
                .productName(product1.getName())
                .productUnit(product1.getUnit().getName())
                .quantity(item1Quantity)
                .price(product1.getPurchasePrice())
                .total(product1.getPurchasePrice().multiply(BigDecimal.valueOf(item1Quantity)))
                .build();

        var item2Quantity = 10L;
        var item2 = PurchaseItem.builder()
                .product(product2)
                .productName(product2.getName())
                .productUnit(product2.getUnit().getName())
                .quantity(item2Quantity)
                .price(product2.getPurchasePrice())
                .total(product2.getPurchasePrice().multiply(BigDecimal.valueOf(item2Quantity)))
                .build();

        var items = List.of(
                item1,
                item2
        );

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

        var foundPurchase = purchaseRepository.findById(purchase.getId());
        assertTrue(foundPurchase.isPresent());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesByDateRangeSpecificationShouldReturnList() {
        var startDate = LocalDateTime.of(2023, Month.MAY, 1, 0,0);
        var endDate = LocalDateTime.of(2023, Month.MAY, 28, 0, 0);
        var specification = PurchaseSpecifications.byDateRange(startDate, endDate);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(4, purchases.size());
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
}
