package com.jcanseco.inventoryapi.persistence;

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

@DisplayName("Purchases Repository Tests")
@SpringBootTest
public class PurchasesTests {

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
                UnitOfMeasurement.builder()
                        .name("Box")
                        .build()
        );

        var category = categoryRepository.saveAndFlush(
                Category.builder()
                        .name("Video Games")
                        .build()
        );

        var supplierAddress = SupplierAddress.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        supplier = supplierRepository.saveAndFlush(Supplier
                .builder()
                .companyName("Pika Games")
                .contactName("Perla Lopez")
                .contactPhone("555-1234-1")
                .address(supplierAddress)
                .build()
        );

        var product1 = productRepository.saveAndFlush(Product.builder()
                .name("Halo 3")
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .quantity(0L)
                .purchasePrice(BigDecimal.valueOf(5.99))
                .salePrice(BigDecimal.valueOf(9.99))
                .build()
        );

        var product2 = productRepository.saveAndFlush(Product.builder()
                .name("Halo Infinite")
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .quantity(0L)
                .purchasePrice(BigDecimal.valueOf(39.99))
                .salePrice(BigDecimal.valueOf(59.99))
                .build()
        );

        products = List.of(product1, product2);
    }


    private PurchaseItem productToItem(Product product) {
        return PurchaseItem.builder()
                .product(product)
                .productName(product.getName())
                .productUnit(product.getUnit().getName())
                .quantity(10L)
                .price(product.getPurchasePrice())
                .total(product.getPurchasePrice().multiply(BigDecimal.valueOf(10L)))
                .build();
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

        var purchase = Purchase.builder()
                .supplier(supplier)
                .items(items)
                .total(total)
                .build();

        var newPurchase = purchaseRepository.save(purchase);
        assertNotNull(newPurchase);
        assertNotNull(newPurchase.getId());
        assertEquals(total, newPurchase.getTotal());
        assertEquals(supplier.getId(), purchase.getSupplier().getId());
        assertNotNull(newPurchase.getItems());
    }
}
