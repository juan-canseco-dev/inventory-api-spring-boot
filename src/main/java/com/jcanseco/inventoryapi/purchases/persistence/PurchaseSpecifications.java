package com.jcanseco.inventoryapi.purchases.persistence;

import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.orders.domain.Order;
import com.jcanseco.inventoryapi.purchases.domain.Purchase;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import java.time.LocalDateTime;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class PurchaseSpecifications {

    public static Specification<Purchase> bySupplier(Supplier supplier) {
        return (root, query, builder) -> builder.equal(root.get("supplier"), supplier);
    }

    public static Specification<Purchase> byArrivedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> builder.between(root.get("arrivedAt"), startDate, endDate);
    }

    public static Specification<Purchase> byOrderedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> builder.between(root.get("orderedAt"), startDate, endDate);
    }

    public static Specification<Purchase> byArrived(boolean arrived) {
        return (root, query, builder) -> builder.equal(root.get("arrived"), arrived);
    }

    public static Specification<Purchase> orderByIdAsc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByIdDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByTotalAsc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("total"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByTotalDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("total"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderBySupplierAsc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            Join<Purchase, Supplier> supplierJoin = root.join("supplier", JoinType.LEFT);
            query.orderBy(builder.asc(supplierJoin.get("companyName")));
            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderBySupplierDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            Join<Purchase, Supplier> supplierJoin = root.join("supplier", JoinType.LEFT);
            query.orderBy(builder.desc(supplierJoin.get("companyName")));
            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByOrderedAtAsc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("orderedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByOrderedAtDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("orderedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByArrivedAtAsc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("arrivedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByArrivedAtDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("arrivedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByArrivedAsc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("arrived"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByArrivedDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("arrived"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }
}






