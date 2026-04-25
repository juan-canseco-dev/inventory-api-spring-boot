package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.persistence.CustomerReportsRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Testcontainers
@Import(TestcontainersConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerReportsRepositoryTests {

    @Autowired
    private CustomerReportsRepository customerReportsRepository;

    @Test
    @Sql("/dashboard-data.sql")
    public void getTopCustomerByRevenueShouldReturnOrderedList() {
        var topCustomers = customerReportsRepository.getTopCustomerByRevenue(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 2, 1, 0, 0),
                Pageable.ofSize(2)
        );

        assertEquals(2, topCustomers.size());
        assertEquals(2L, topCustomers.get(0).id());
        assertEquals("Jane Smith", topCustomers.get(0).fullName());
        assertEquals(0, topCustomers.get(0).totalRevenue().compareTo(BigDecimal.valueOf(600)));
        assertEquals(1L, topCustomers.get(1).id());
        assertEquals("John Doe", topCustomers.get(1).fullName());
        assertEquals(0, topCustomers.get(1).totalRevenue().compareTo(BigDecimal.valueOf(510)));
    }
}
