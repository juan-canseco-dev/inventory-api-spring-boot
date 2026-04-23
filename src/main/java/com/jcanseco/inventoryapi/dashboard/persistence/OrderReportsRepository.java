package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.orders.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface OrderReportsRepository extends JpaRepository<Order, Long> {
    @Query("""
    SELECT COALESCE(SUM(o.total), 0)
    FROM Order o
    WHERE o.deliveredAt >= :startDate
    AND   o.deliveredAt < :endDate
    """)
    BigDecimal getOrdersSummaryByPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate
    );
}
