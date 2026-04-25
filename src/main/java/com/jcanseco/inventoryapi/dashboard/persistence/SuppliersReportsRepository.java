package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.dashboard.dto.TopSupplierByRevenueDto;
import com.jcanseco.inventoryapi.purchases.domain.Purchase;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SuppliersReportsRepository extends JpaRepository<Supplier, Long> {
    @Query("""
    SELECT NEW com.jcanseco.inventoryapi.dashboard.dto.TopSupplierByRevenueDto(
        s.id,
        s.companyName,
        SUM(p.total)
    )
    FROM Purchase p
    JOIN p.supplier s
    WHERE p.arrived = true
    AND (:startDate IS NULL OR p.arrivedAt >= :startDate)
    AND (:endDate IS NULL OR p.arrivedAt < :endDate)
    GROUP BY s.id, s.companyName
    ORDER BY SUM(p.total) DESC
""")
    List<TopSupplierByRevenueDto> getTopSuppliersByRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
