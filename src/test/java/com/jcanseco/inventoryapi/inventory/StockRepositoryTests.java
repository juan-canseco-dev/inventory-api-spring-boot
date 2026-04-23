package com.jcanseco.inventoryapi.inventory;

import com.jcanseco.inventoryapi.inventory.stock.persistence.StockRepository;
import com.jcanseco.inventoryapi.inventory.stock.persistence.StockSpecifications;
import java.util.List;
import com.jcanseco.inventoryapi.shared.testing.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@Import(TestcontainersConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StockRepositoryTests {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @Sql("/multiple-products.sql")
    public void getStocksByProductIdsShouldReturnStocksList() {

       var productIds = List.of(
                1L,
                4L,
                10L
        );

        var stocks = stockRepository.findAll(
                StockSpecifications.byProductIds(productIds)
        );

        assertNotNull(stocks);
        assertEquals(3, stocks.size());
    }

}






