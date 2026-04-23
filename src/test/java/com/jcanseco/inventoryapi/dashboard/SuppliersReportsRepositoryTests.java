package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.persistence.SuppliersReportsRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Testcontainers
@Import(TestcontainersConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SuppliersReportsRepositoryTests {

    @Autowired
    private SuppliersReportsRepository suppliersReportsRepository;

    @Test
    @Sql("/dashboard-data.sql")
    public void getTopSuppliersByRevenueShouldReturnOrderedList() {
        var topSuppliers = suppliersReportsRepository.getTopSuppliersByRevenue(Pageable.ofSize(2));

        assertEquals(2, topSuppliers.size());
        assertEquals(2L, topSuppliers.get(0).id());
        assertEquals("Tech Solutions Inc", topSuppliers.get(0).name());
        assertEquals(0, topSuppliers.get(0).totalRevenue().compareTo(BigDecimal.valueOf(600)));
        assertEquals(1L, topSuppliers.get(1).id());
        assertEquals("ABC Corp", topSuppliers.get(1).name());
        assertEquals(0, topSuppliers.get(1).totalRevenue().compareTo(BigDecimal.valueOf(510)));
    }
}
