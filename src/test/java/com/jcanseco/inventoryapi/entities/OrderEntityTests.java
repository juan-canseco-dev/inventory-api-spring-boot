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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderEntityTests {

    private Customer customer;
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

        var supplier = Supplier.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        customer = Customer.builder()
                .id(1L)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
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
    public void createOrderItemsAndTotalShouldBeExpected() {
        var expectedTotal = new BigDecimal("1900.00");
        var order = Order.createNew(customer, products, productsWithQuantities);
        assertEquals(customer, order.getCustomer());
        assertEquals(expectedTotal, order.getTotal());
        orderItemsEqualsToProductsWithQuantities(order.getItems(), products, productsWithQuantities);
    }


    @Test
    public void updateOrderWhenOrderIsNotDeliveredShouldUpdate() {
        var expectedTotal = new BigDecimal("950.00");
        var order = Order.createNew(customer, products, productsWithQuantities);
        order.update(products, productsWithQuantitiesForUpdate);
        assertEquals(expectedTotal, order.getTotal());
        orderItemsEqualsToProductsWithQuantities(order.getItems(), products, productsWithQuantitiesForUpdate);
    }

    @Test
    public void updateOrderWhenOrderIsDeliveredShouldThrowException() {
        var order = Order.createNew(customer, products, productsWithQuantities);
        order.deliver();
        assertThrows(DomainException.class, () -> order.update(products, productsWithQuantitiesForUpdate));
    }

    @Test
    public void markOrderAsDeliveredWhenOrderIsNotDeliveredShouldMark() {
        var order = Order.createNew(customer, products, productsWithQuantities);
        order.deliver();
        assertTrue(order.isDelivered());
        assertNotNull(order.getDeliveredAt());
    }

    @Test
    public void markOrderAsDeliveredWhenOrderIsDeliveredShouldThrowException() {
        var order = Order.createNew(customer, products, productsWithQuantities);
        order.deliver();
        assertThrows(DomainException.class, order::deliver);
    }

    private void orderItemsEqualsToProductsWithQuantities(List<OrderItem> items, List<Product> products, HashMap<Long, Long> productsWithQuantities) {

        assertNotNull(items);

        Map<Long, Product> productsMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        for (OrderItem item : items) {
            var productId = item.getProductId();
            var product = productsMap.get(productId);
            var productQuantity = productsWithQuantities.get(productId);
            var expectedTotal = product.getSalePrice().multiply(BigDecimal.valueOf(productQuantity));
            assertEquals(product.getId(), item.getProductId());
            assertEquals(product.getName(), item.getProductName());
            assertEquals(product.getUnit().getName(), item.getProductUnit());
            assertEquals(product.getSalePrice(), item.getPrice());
            assertEquals(productQuantity, item.getQuantity());
            assertEquals(expectedTotal, item.getTotal());
        }
    }


}
