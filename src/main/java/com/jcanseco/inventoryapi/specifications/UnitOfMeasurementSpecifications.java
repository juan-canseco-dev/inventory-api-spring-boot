package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import org.springframework.data.jpa.domain.Specification;

public class UnitOfMeasurementSpecifications {
    public static Specification<UnitOfMeasurement> orderBy(
            Specification<UnitOfMeasurement> spec,
            String orderBy,
            boolean ascending) {
        return (root, query, builder) -> {
            var order = ascending? builder.asc(root.get(orderBy)) : builder.desc(root.get(orderBy));
            query.orderBy(order);
            return spec.toPredicate(root, query, builder);
        };
    }
    public static Specification<UnitOfMeasurement> byNameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
}
