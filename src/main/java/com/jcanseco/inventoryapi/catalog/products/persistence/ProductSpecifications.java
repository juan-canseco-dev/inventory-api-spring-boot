package com.jcanseco.inventoryapi.catalog.products.persistence;

import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.units.domain.UnitOfMeasurement;
import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
            Join<Product, Supplier> supplierJoin = root.join("supplier", JoinType.LEFT);

            query.orderBy(builder.asc(supplierJoin.get("companyName")));

            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderBySupplierDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            Join<Product, Supplier> supplierJoin = root.join("supplier", JoinType.LEFT);

            query.orderBy(builder.desc(supplierJoin.get("companyName")));

            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByCategoryAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
            query.orderBy(builder.asc(categoryJoin.get("name")));
            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByCategoryDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
            query.orderBy(builder.desc(categoryJoin.get("name")));
            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByUnitAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            Join<Product, UnitOfMeasurement> unitJoin = root.join("unit", JoinType.LEFT);
            query.orderBy(builder.asc(unitJoin.get("name")));
            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByUnitDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
           Join<Product, UnitOfMeasurement> unitJoin = root.join("unit", JoinType.LEFT);
           query.orderBy(builder.desc(unitJoin.get("name")));
           return spec == null
                   ? builder.conjunction()
                   : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByStockAsc(Specification<Product> spec) {
        return (root, query, builder) -> {
            Join<Product, Stock> stockJoin = root.join("stock", JoinType.LEFT);
            query.orderBy(builder.asc(stockJoin.get("quantity")));
            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Product> orderByStockDesc(Specification<Product> spec) {
        return (root, query, builder) -> {
            Join<Product, Stock> stockJoin = root.join("stock", JoinType.LEFT);
            query.orderBy(builder.desc(stockJoin.get("quantity")));
            return spec == null
                    ? builder.conjunction()
                    : spec.toPredicate(root, query, builder);
        };
    }
}






