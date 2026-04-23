package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.dto.ProductWithLowStockDto;
import com.jcanseco.inventoryapi.dashboard.dto.TopSoldProductDto;
import com.jcanseco.inventoryapi.dashboard.persistence.ProductReportsRepository;
import com.jcanseco.inventoryapi.shared.testing.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Testcontainers
@Import(TestcontainersConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductReportsRepositoryTests {

    @Autowired
    private ProductReportsRepository productReportsRepository;

    @Test
    @Sql("/dashboard-data.sql")
    public void getTotalInventoryValueShouldReturnSummary() {
        var totalInventoryValue = productReportsRepository.getTotalInventoryValue();
        assertEquals(0, totalInventoryValue.compareTo(BigDecimal.valueOf(1300)));
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getProductsWithLowStockShouldReturnOrderedList() {
        var products = productReportsRepository.getProductsWithLowStock(5L, Pageable.ofSize(2));

        var expectedProducts = List.of(
                new ProductWithLowStockDto(4L, "Chair", 1L),
                new ProductWithLowStockDto(2L, "Mouse", 2L)
        );

        assertEquals(expectedProducts, products);
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getTopSoldProductsShouldReturnOrderedList() {
        var topSoldProducts = productReportsRepository.getTopSoldProducts(Pageable.ofSize(2));

        var expectedProducts = List.of(
                new TopSoldProductDto(3L, "Desk", 5L),
                new TopSoldProductDto(1L, "Laptop", 4L)
        );

        assertEquals(expectedProducts, topSoldProducts);
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getProductsWithLowStockCountShouldReturnCount() {
        var productsWithLowStockCount = productReportsRepository.getProductsWithLowStockCount(5L);
        assertEquals(3L, productsWithLowStockCount);
    }
}
