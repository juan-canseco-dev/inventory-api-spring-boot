package com.jcanseco.inventoryapi.persistence.purchases;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Create Purchase Persistence Tests")
@SpringBootTest
public class CreatePurchaseTests {

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

    private Supplier supplier;
    private List<Product> products;

    @BeforeEach
    public void setup() {

        var unit = unitRepository.saveAndFlush(
                newUnit("Box")
        );

        var category = categoryRepository.saveAndFlush(
                newCategory("Video Games")
        );

        supplier = supplierRepository.saveAndFlush(
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

        var product1 = productRepository.saveAndFlush(
                newProduct(
                        supplier,
                        category,
                        unit,
                        "Halo 3",
                        5.99,
                        9.99
                )
        );

        var product2 = productRepository.saveAndFlush(

                newProduct(
                        supplier,
                        category,
                        unit,
                        "Halo Infinite",
                        39.99,
                        59.99
                )
        );

        products = List.of(product1, product2);
    }


    private PurchaseItem productToItem(Product product) {
        return newPurchaseItem(product, 10L);
    }

    @AfterEach
    public void cleanup() {
        purchaseRepository.deleteAll();
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    public void createPurchaseShouldGenerateId() {

        var items = products.stream()
                .map(this::productToItem)
                .toList();

        var total = items.stream()
                .map(PurchaseItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var purchase = newPurchase(supplier, items);
        var newPurchase = purchaseRepository.save(purchase);

        assertNotNull(newPurchase);
        assertNotNull(newPurchase.getId());

        assertEquals(total, newPurchase.getTotal());
        assertEquals(supplier.getId(), purchase.getSupplier().getId());

        assertNotNull(newPurchase.getItems());
        assertNotNull(newPurchase.getCreatedAt());
    }
}
