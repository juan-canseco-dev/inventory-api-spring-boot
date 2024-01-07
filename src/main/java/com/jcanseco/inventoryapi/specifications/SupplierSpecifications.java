package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecifications {

    public static Specification<Supplier> orderBy(Specification<Supplier> spec, String orderBy, boolean ascending) {
        return (root, query, builder) -> {
            var order = ascending? builder.asc(root.get(orderBy)) : builder.desc(root.get(orderBy));
            query.orderBy(order);
            return spec.toPredicate(root, query, builder);
        };
    }
    public static Specification<Supplier> byCompanyNameLike(String companyName) {
        return (root, query, builder) -> builder.like(root.get("companyName"), "%" + companyName + "%");
    }

    public static Specification<Supplier> byContactNameLike(String contactName) {
        return (root, query, builder) -> builder.like(root.get("contactName"), "%" + contactName + "%");
    }
    public static Specification<Supplier> byContactPhoneLike(String contactPhone) {
        return (root, query, builder) -> builder.like(root.get("contactPhone"), "%" + contactPhone + "%");
    }
}
