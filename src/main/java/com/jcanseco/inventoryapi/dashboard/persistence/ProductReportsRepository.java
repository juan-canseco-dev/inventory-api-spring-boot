package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.dashboard.dto.ProductsByCategoryDto;
import com.jcanseco.inventoryapi.dashboard.dto.ProductWithLowStockDto;
import com.jcanseco.inventoryapi.dashboard.dto.TopSoldProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ProductReportsRepository extends JpaRepository<Product, Long> {
    @Query("""
            SELECT COALESCE(SUM(p.purchasePrice * s.quantity), 0)
            FROM Product p
            INNER JOIN p.stock s""")
    BigDecimal getTotalInventoryValue();

    @Query("""
     SELECT NEW com.jcanseco.inventoryapi.dashboard.dto.ProductWithLowStockDto(
          p.id,
          p.name,
          s.quantity
          )
          FROM Product p INNER JOIN p.stock s
          WHERE s.quantity <= :stockThreshold
          ORDER BY s.quantity ASC
    \s""")
    List<ProductWithLowStockDto> getProductsWithLowStock(
            @Param("stockThreshold") Long stockThreshold,
            Pageable pageable
    );

    @Query("""
    SELECT new com.jcanseco.inventoryapi.dashboard.dto.TopSoldProductDto(
         p.id,
         p.name,
         SUM(oi.quantity)
        )
        FROM OrderItem oi
        INNER JOIN oi.product p
        INNER JOIN oi.order o
        WHERE o.delivered = true
        AND (:startDate IS NULL OR o.deliveredAt >= :startDate)
        AND (:endDate IS NULL OR o.deliveredAt < :endDate)
        GROUP BY p.id, p.name
        ORDER BY SUM(oi.quantity) DESC""")
    List<TopSoldProductDto> getTopSoldProducts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
    SELECT COALESCE(COUNT(p), 0)
    FROM Product p INNER JOIN p.stock s
    WHERE s.quantity <= :stockThreshold
    """)
    Long getProductsWithLowStockCount(@Param("stockThreshold") Long stockThreshold);

    @Query("""
    SELECT COALESCE(COUNT(p), 0)
    FROM Product p INNER JOIN p.stock s
    WHERE s.quantity = 0
    """)
    Long getOutOfStockProductsCount();

    @Query("""
    SELECT new com.jcanseco.inventoryapi.dashboard.dto.ProductsByCategoryDto(
        c.id,
        c.name,
        COUNT(p)
    )
    FROM Product p
    INNER JOIN p.category c
    GROUP BY c.id, c.name
    ORDER BY COUNT(p) DESC, c.name ASC
    """)
    List<ProductsByCategoryDto> getProductsCountByCategory();

}
