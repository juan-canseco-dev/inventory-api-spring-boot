package com.jcanseco.inventoryapi.dashboard.persistence;

import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.dashboard.dto.ProductWithLowStockDto;
import com.jcanseco.inventoryapi.dashboard.dto.TopSoldProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
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
        GROUP BY p.id, p.name
        ORDER BY SUM(oi.quantity) DESC""")
    List<TopSoldProductDto> getTopSoldProducts(Pageable pageable);

    @Query("""
    SELECT COALESCE(COUNT(p), 0)
    FROM Product p INNER JOIN p.stock s
    WHERE s.quantity <= :stockThreshold
    """)
    Long getProductsWithLowStockCount(@Param("stockThreshold") Long stockThreshold);

}
