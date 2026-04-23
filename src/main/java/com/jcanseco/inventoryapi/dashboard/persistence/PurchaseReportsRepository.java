package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.purchases.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface PurchaseReportsRepository extends JpaRepository<Purchase, Long> {
    @Query(value = """
            SELECT COALESCE(SUM(p.total), 0)\s
            FROM Purchase p
            WHERE p.arrivedAt >= :startDate
            AND   p.arrivedAt < :endDate
           \s"""
    )
    BigDecimal getPurchasesSummaryByPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate
    );
}
