package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {
    public static Specification<Customer> orderBy(Specification<Customer> spec, String orderBy, boolean ascending) {
        return (root, query, builder) -> {
            var order = ascending? builder.asc(root.get(orderBy)) : builder.desc(root.get(orderBy));
            query.orderBy(order);
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Customer> byDniLike(String dni) {
        return (root, query, builder) -> builder.like(root.get("dni"), "%" + dni + "%");
    }

    public static Specification<Customer> byPhoneLike(String phone) {
        return (root, query, builder) -> builder.like(root.get("phone"), "%" + phone + "%");
    }

    public static Specification<Customer> byFullNameLike(String fullName) {
        return (root, query, builder) -> builder.like(root.get("fullName"), "%" + fullName + "%");
    }
}
