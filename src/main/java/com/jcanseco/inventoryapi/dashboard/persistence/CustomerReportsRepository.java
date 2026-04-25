package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.dashboard.dto.TopCustomerByRevenueDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerReportsRepository extends JpaRepository<Customer, Long> {

    @Query("""
    SELECT new com.jcanseco.inventoryapi.dashboard.dto.TopCustomerByRevenueDto(
        c.id,
        c.fullName,
        SUM(o.total)
    )
    FROM Order o
    INNER JOIN o.customer c
    WHERE o.delivered = true
    AND (:startDate IS NULL OR o.deliveredAt >= :startDate)
    AND (:endDate IS NULL OR o.deliveredAt < :endDate)
    GROUP BY  c.id, c.fullName
    ORDER BY SUM(o.total) DESC
    """)
    List<TopCustomerByRevenueDto> getTopCustomerByRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
