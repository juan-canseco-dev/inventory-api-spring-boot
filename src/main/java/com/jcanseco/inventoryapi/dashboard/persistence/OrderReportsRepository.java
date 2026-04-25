package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.orders.domain.Order;
import com.jcanseco.inventoryapi.dashboard.dto.MonthlySalesPointDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Query("""
    SELECT new com.jcanseco.inventoryapi.dashboard.dto.MonthlySalesPointDto(
        YEAR(o.deliveredAt),
        MONTH(o.deliveredAt),
        COALESCE(SUM(o.total), 0)
    )
    FROM Order o
    WHERE o.delivered = true
    AND (:startDate IS NULL OR o.deliveredAt >= :startDate)
    AND (:endDate IS NULL OR o.deliveredAt < :endDate)
    GROUP BY YEAR(o.deliveredAt), MONTH(o.deliveredAt)
    ORDER BY YEAR(o.deliveredAt), MONTH(o.deliveredAt)
    """)
    List<MonthlySalesPointDto> getMonthlySalesSummary(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
