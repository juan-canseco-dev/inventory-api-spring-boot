package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecifications {
    public static Specification<Category> orderBy(Specification<Category> spec, String orderBy, boolean ascending) {
        return (root, query, builder) -> {
            var order = ascending? builder.asc(root.get(orderBy)) : builder.desc(root.get(orderBy));
            query.orderBy(order);
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Category> byNameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
}
