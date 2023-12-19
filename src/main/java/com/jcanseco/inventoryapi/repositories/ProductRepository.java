package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    interface Specs {
        static Specification<Product> bySupplier(Supplier supplier) {
            return (root, query, builder) -> builder.equal(root.get("supplier"), supplier);
        }
        static Specification<Product> byCategory(Category category) {
            return (root, query, builder) -> builder.equal(root.get("category"), category);
        }
        static Specification<Product> byUnit(UnitOfMeasurement unit) {
            return (root, query, builder) -> builder.equal(root.get("unit"), unit);
        }
        static Specification<Product> byNameContaining(String name) {
            return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
        }
        @Nullable
        static Specification<Product> byAllFilters(Supplier supplier,
                                                   Category category,
                                                   UnitOfMeasurement unit,
                                                   String name) {

            var specs = new ArrayList<Specification<Product>>();

            Optional.ofNullable(supplier).ifPresent(s -> specs.add(bySupplier(s)));
            Optional.ofNullable(category).ifPresent(c -> specs.add(byCategory(c)));
            Optional.ofNullable(unit).ifPresent(u -> specs.add(byUnit(u)));
            Optional.ofNullable(name).filter(StringUtils::hasText).ifPresent(n -> specs.add(byNameContaining(n)));

            return specs.isEmpty()? null : specs.stream().reduce(Specification::and).orElse(null);
        }
    }
}
