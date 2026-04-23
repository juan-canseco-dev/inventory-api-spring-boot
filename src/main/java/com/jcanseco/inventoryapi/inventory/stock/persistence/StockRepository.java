package com.jcanseco.inventoryapi.inventory.stock.persistence;

import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
    @Query("SELECT s from Stock s WHERE s.productId in :ids")
    List<Stock> getStockByProducts(@Param("ids") List<Long> ids);
    Optional<Stock> findByProductId(Long productId);
}






