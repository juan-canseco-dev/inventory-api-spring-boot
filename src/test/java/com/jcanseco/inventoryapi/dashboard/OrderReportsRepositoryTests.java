package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.persistence.OrderReportsRepository;
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
public class OrderReportsRepositoryTests {

    @Autowired
    private OrderReportsRepository orderReportsRepository;

    @Test
    @Sql("/dashboard-data.sql")
    public void getOrdersSummaryByPeriodShouldReturnTotal() {
        var totalValue = orderReportsRepository.getOrdersSummaryByPeriod(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 2, 1, 0, 0)
        );

        assertEquals(0, totalValue.compareTo(BigDecimal.valueOf(1110)));
    }

    @Test
    @Sql("/dashboard-data.sql")
    public void getMonthlySalesSummaryShouldReturnOrderedSeries() {
        var monthlySales = orderReportsRepository.getMonthlySalesSummary(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 3, 1, 0, 0)
        );

        assertEquals(2, monthlySales.size());
        assertEquals(2024, monthlySales.get(0).year());
        assertEquals(1, monthlySales.get(0).month());
        assertEquals(0, monthlySales.get(0).totalValue().compareTo(BigDecimal.valueOf(1110)));
        assertEquals(2024, monthlySales.get(1).year());
        assertEquals(2, monthlySales.get(1).month());
        assertEquals(0, monthlySales.get(1).totalValue().compareTo(BigDecimal.valueOf(480)));
    }
}
