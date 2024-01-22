package com.jcanseco.inventoryapi.entities;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseEntityTests {
    private Supplier supplier;
    private List<Product> products;
    private final HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
        put(9L, 10L);
        put(10L, 10L);
    }};

    private final HashMap<Long, Long> productsWithQuantitiesForUpdate = new HashMap<>() {{
        put(9L, 5L);
        put(10L, 5L);
    }};

    @BeforeEach
    public void setup() {
        var unit = UnitOfMeasurement.builder()
                .id(1L)
                .name("Piece")
                .build();

        var category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        var address = Address.builder()
                .country("United States")
                .state("California")
                .city("San Francisco")
                .zipCode("94105")
                .street("123 Main St")
                .build();

        supplier = Supplier.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        products = List.of(
                Product.builder()
                        .id(9L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Vacuum Cleaner")
                        .purchasePrice(new BigDecimal("90.00"))
                        .salePrice(new BigDecimal("150.00"))
                        .build(),
                Product.builder()
                        .id(10L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Toaster")
                        .purchasePrice(new BigDecimal("25.00"))
                        .salePrice(new BigDecimal("40.00"))
                        .build()
        );
    }

    @Test
    public void createPurchaseItemsAndTotalShouldBeExpected() {
        var expectedTotal = new BigDecimal("1150.00");
        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        assertEquals(supplier, purchase.getSupplier());
        assertEquals(expectedTotal, purchase.getTotal());
        purchaseItemsEqualsToProductsWithQuantities(purchase.getItems(), products, productsWithQuantities);
    }


    @Test
    public void updatePurchaseWhenPurchaseIsNotArrivedShouldUpdate() {
        var expectedTotal = new BigDecimal("575.00");
        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.update(products, productsWithQuantitiesForUpdate);
        assertEquals(expectedTotal, purchase.getTotal());
        purchaseItemsEqualsToProductsWithQuantities(purchase.getItems(), products, productsWithQuantitiesForUpdate);
    }

    @Test
    public void updatePurchaseWhenPurchaseIsArrivedShouldThrowException() {
        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.markAsArrived();
        assertThrows(DomainException.class, () -> purchase.update(products, productsWithQuantitiesForUpdate));
    }

    @Test
    public void markPurchaseAsArrivedWhenPurchaseIsNotArrivedShouldMark() {
        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.markAsArrived();
        assertTrue(purchase.isArrived());
        assertNotNull(purchase.getArrivedAt());
    }

    @Test
    public void markPurchaseAsArrivedWhenPurchaseIsArrivedShouldThrowException() {
        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.markAsArrived();
        assertThrows(DomainException.class, purchase::markAsArrived);
    }

    private void purchaseItemsEqualsToProductsWithQuantities(List<PurchaseItem> items, List<Product> products, HashMap<Long, Long> productsWithQuantities) {

        assertNotNull(items);

        Map<Long, Product> productsMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        for (PurchaseItem item : items) {
            var productId = item.getProductId();
            var product = productsMap.get(productId);
            var productQuantity = productsWithQuantities.get(productId);
            var expectedTotal = product.getPurchasePrice().multiply(BigDecimal.valueOf(productQuantity));
            assertEquals(product.getId(), item.getProductId());
            assertEquals(product.getName(), item.getProductName());
            assertEquals(product.getUnit().getName(), item.getProductUnit());
            assertEquals(product.getPurchasePrice(), item.getPrice());
            assertEquals(productQuantity, item.getQuantity());
            assertEquals(expectedTotal, item.getTotal());
        }
    }
}
