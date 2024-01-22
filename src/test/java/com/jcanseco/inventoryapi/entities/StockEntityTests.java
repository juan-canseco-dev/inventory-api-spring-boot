package com.jcanseco.inventoryapi.entities;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class StockEntityTests {
    private Stock stock;

    @BeforeEach
    public void setup() {
        stock = Stock.builder()
                .id(1L)
                .productId(1L)
                .quantity(10L)
                .build();
    }

    @Test
    public void addStockShouldUpdateQuantity() {
        var expectedStock = 15L;
        stock.addStock(5L);
        assertEquals(expectedStock, stock.getQuantity());
    }

    @Test
    public void removeStockShouldUpdateQuantity() {
        var expectedStock = 5L;
        stock.removeStock(5L);
        assertEquals(expectedStock, stock.getQuantity());
    }

    @Test
    public void removeStockWhenQuantityIsGreaterThanCurrentStockShouldThrowException() {
        assertThrows(DomainException.class, () -> stock.removeStock(11L));
    }
}
