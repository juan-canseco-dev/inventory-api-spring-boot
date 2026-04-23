package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.dashboard.dto.TopSupplierByRevenueDto;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SuppliersReportsRepository extends JpaRepository<Supplier, Long> {
    @Query("""
    SELECT NEW com.jcanseco.inventoryapi.dashboard.dto.TopSupplierByRevenueDto(
        s.id,
        s.companyName,
        SUM(oi.total)
    )
    FROM OrderItem oi
    JOIN oi.product p
    JOIN p.supplier s
    JOIN oi.order o
    WHERE o.delivered = true
    GROUP BY s.id, s.companyName
    ORDER BY SUM(oi.total) DESC
""")
    List<TopSupplierByRevenueDto> getTopSuppliersByRevenue(Pageable pageable);
}
