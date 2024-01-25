package com.jcanseco.inventoryapi.specifications;


import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecifications {
    public static Specification<Order> byCustomer(Customer customer) {
        return (root, query, builder) -> builder.equal(root.get("customer"), customer);
    }

    public static Specification<Order> byDeliveredBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> builder.between(root.get("deliveredAt"), startDate, endDate);
    }

    public static Specification<Order> byOrderedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> builder.between(root.get("orderedAt"), startDate, endDate);
    }

    public static Specification<Order> byDelivered(boolean delivered) {
        return (root, query, builder) -> builder.equal(root.get("delivered"), delivered);
    }

    public static Specification<Order> orderByIdAsc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByIdDesc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByTotalAsc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("total"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByTotalDesc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("total"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByCustomerAsc(Specification<Order> spec) {
        return (root, query, builder) -> {
            root.fetch("customer");
            query.orderBy(
                    builder.asc(root.get("customer").get("fullName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByCustomerDesc(Specification<Order> spec) {
        return (root, query, builder) -> {
            root.fetch("customer");
            query.orderBy(
                    builder.desc(root.get("customer").get("fullName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByOrderedAtAsc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("orderedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByOrderedAtDesc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("orderedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByDeliveredAtAsc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("deliveredAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByDeliveredAtDesc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("deliveredAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByDeliveredAsc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("delivered"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Order> orderByDeliveredDesc(Specification<Order> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("delivered"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }
}
