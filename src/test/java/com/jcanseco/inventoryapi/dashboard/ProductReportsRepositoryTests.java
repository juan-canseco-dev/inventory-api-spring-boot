package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.dto.ProductsByCategoryDto;
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
import java.time.LocalDateTime;
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
                new ProductWithLowStockDto(5L, "Keyboard", 0L),
                new ProductWithLowStockDto(4L, "Chair", 1L)
        );

        assertEquals(expectedProducts, products);
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getTopSoldProductsShouldReturnOrderedList() {
        var topSoldProducts = productReportsRepository.getTopSoldProducts(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 2, 1, 0, 0),
                Pageable.ofSize(2)
        );

        var expectedProducts = List.of(
                new TopSoldProductDto(3L, "Desk", 5L),
                new TopSoldProductDto(2L, "Mouse", 3L)
        );

        assertEquals(expectedProducts, topSoldProducts);
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getProductsWithLowStockCountShouldReturnCount() {
        var productsWithLowStockCount = productReportsRepository.getProductsWithLowStockCount(5L);
        assertEquals(4L, productsWithLowStockCount);
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getOutOfStockProductsCountShouldReturnCount() {
        var outOfStockProductsCount = productReportsRepository.getOutOfStockProductsCount();
        assertEquals(1L, outOfStockProductsCount);
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getProductsCountByCategoryShouldReturnOrderedList() {
        var productsByCategory = productReportsRepository.getProductsCountByCategory();

        var expectedProductsByCategory = List.of(
                new ProductsByCategoryDto(1L, "Electronics", 3L),
                new ProductsByCategoryDto(2L, "Furniture", 2L)
        );

        assertEquals(expectedProductsByCategory, productsByCategory);
    }
}
