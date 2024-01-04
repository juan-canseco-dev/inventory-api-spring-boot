package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category>
{
    List<Category> findAllByNameContaining(String name, Sort sort);
    Page<Category> findAllByNameContaining(String name, Pageable pageable);

    interface Specs {
        static Specification<Category> orderByName(Specification<Category> spec, boolean ascending) {
            return (root, query, builder) -> {
                var order = ascending? builder.asc(root.get("name")) : builder.desc(root.get("name"));
                query.orderBy(order);
                return spec.toPredicate(root, query, builder);
            };
        }
        static Specification<Category> orderById(Specification<Category> spec, boolean ascending) {
            return (root, query, builder) -> {
                var order = ascending? builder.asc(root.get("id")) : builder.desc(root.get("id"));
                query.orderBy(order);
                return spec.toPredicate(root, query, builder);
            };
        }

        static Specification<Category> byNameLike(String name) {
            return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
        }

    }
}
