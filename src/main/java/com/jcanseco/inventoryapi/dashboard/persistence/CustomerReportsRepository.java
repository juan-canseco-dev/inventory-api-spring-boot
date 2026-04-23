package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.dashboard.dto.TopCustomerByRevenueDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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
    GROUP BY  c.id, c.fullName
    ORDER BY SUM(o.total) DESC
    """)
    List<TopCustomerByRevenueDto> getTopCustomerByRevenue(Pageable pageable);
}
