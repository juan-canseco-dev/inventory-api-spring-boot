package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.persistence.PurchaseReportsRepository;
import com.jcanseco.inventoryapi.shared.testing.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Testcontainers
@Import(TestcontainersConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PurchaseReportsRepositoryTests {

    @Autowired
    private PurchaseReportsRepository purchaseReportsRepository;

    @Test
    @Sql("/dashboard-data.sql")
    public void getPurchasesSummaryByPeriodShouldReturnTotal() {
        var totalValue = purchaseReportsRepository.getPurchasesSummaryByPeriod(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 2, 1, 0, 0)
        );

        assertEquals(0, totalValue.compareTo(BigDecimal.valueOf(580)));
    }
}
