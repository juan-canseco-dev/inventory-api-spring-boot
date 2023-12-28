package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;
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
                newUnit("Box")
        );

        var category = categoryRepository.saveAndFlush(
                newCategory("Video Games")
        );

        var supplier = supplierRepository.saveAndFlush(
                newSupplier(
                        "Pika Games",
                        "Perla Lopez",
                        "555-1234-1",
                        newSupplierAddress(
                                "Mexico",
                                "Sonora",
                                "Hermosillo",
                                "83200",
                                "Center"
                        )
                )
        );

        var products = productRepository.saveAllAndFlush(
                List.of(
                        newProduct(
                                supplier,
                                category,
                                unit,
                                "Halo 3",
                                5.99,
                                9.99
                        ),
                        newProduct(
                                supplier,
                                category,
                                unit,
                                "Halo Infinite",
                                39.99,
                                59.99
                        )
                )
        );

        var items = products.stream().map(p -> newPurchaseItem(p, 10L)).toList();

        var purchase = purchaseRepository.saveAndFlush(
                newPurchase(supplier, items)
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
        var specification = PurchaseRepository.Specs.byDateRange(startDate, endDate);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(4, purchases.size());
    }

    @Test
    @Sql("/multiple-purchases.sql")
    public void getPurchasesBySupplierSpecificationShouldReturnList() {
        var supplier = supplierRepository.findById(2L).orElseThrow();
        var specification = PurchaseRepository.Specs.bySupplier(supplier);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(5, purchases.size());
    }
}
