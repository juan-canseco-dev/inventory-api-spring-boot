package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.StockRepository;
import com.jcanseco.inventoryapi.specifications.StockSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

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
