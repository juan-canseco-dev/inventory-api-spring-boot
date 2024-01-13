package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> bySupplier(Supplier supplier) {
        return (root, query, builder) -> builder.equal(root.get("supplier"), supplier);
    }
    public static Specification<Product> byCategory(Category category) {
        return (root, query, builder) -> builder.equal(root.get("category"), category);
    }
    public static Specification<Product> byUnit(UnitOfMeasurement unit) {
        return (root, query, builder) -> builder.equal(root.get("unit"), unit);
    }

    public static Specification<Product> byNameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> orderByIdAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByIdDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByNameAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByNameDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderBySupplierAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("supplier");
            query.orderBy(
                    builder.asc(root.get("supplier").get("companyName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderBySupplierDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("supplier");
            query.orderBy(
                    builder.desc(root.get("supplier").get("companyName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByCategoryAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("category");
            query.orderBy(
                    builder.asc(root.get("category").get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByCategoryDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("category");
            query.orderBy(
                    builder.desc(root.get("category").get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByUnitAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("unit");
            query.orderBy(
                    builder.asc(root.get("unit").get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByUnitDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("unit");
            query.orderBy(
                    builder.desc(root.get("unit").get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByStockAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("stock");
            query.orderBy(
              builder.asc(root.get("stock").get("quantity"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByStockDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            root.fetch("stock");
            query.orderBy(
                    builder.desc(root.get("stock").get("quantity"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }
}
