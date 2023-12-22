package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Purchase;
import com.jcanseco.inventoryapi.entities.Supplier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {
    interface Specs {
        static Specification<Purchase> bySupplier(Supplier supplier) {
            return (root, query, builder) -> builder.equal(root.get("supplier"), supplier);
        }

        static Specification<Purchase> byDateRange(LocalDateTime startDate, LocalDateTime endDate) {
            return (root, query, builder) -> builder.between(root.get("createdAt"), startDate, endDate);
        }
    }
}
