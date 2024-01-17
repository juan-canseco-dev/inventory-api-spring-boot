package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.Purchase;
import com.jcanseco.inventoryapi.entities.Supplier;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class PurchaseSpecifications {

    public static Specification<Purchase> bySupplier(Supplier supplier) {
        return (root, query, builder) -> builder.equal(root.get("supplier"), supplier);
    }

    public static Specification<Purchase> arrivedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> builder.between(root.get("arrivedAt"), startDate, endDate);
    }

    public static Specification<Purchase> byCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), startDate, endDate);
    }

    public static Specification<Purchase> isArrived() {
        return (root, query, builder) -> builder.equal(root.get("arrived"), true);
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
            root.fetch("supplier");
            query.orderBy(
                    builder.asc(root.get("supplier").get("companyName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderBySupplierDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            root.fetch("supplier");
            query.orderBy(
                    builder.desc(root.get("supplier").get("companyName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByCreatedAtAsc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("createdAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Purchase> orderByCreatedAtDesc(Specification<Purchase> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("createdAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

}
