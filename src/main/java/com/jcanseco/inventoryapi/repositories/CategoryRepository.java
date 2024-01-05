package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category>
{
    interface Specs {
        static Specification<Category> orderBy(Specification<Category> spec, String orderBy, boolean ascending) {
            return (root, query, builder) -> {
                var order = ascending? builder.asc(root.get(orderBy)) : builder.desc(root.get(orderBy));
                query.orderBy(order);
                return spec.toPredicate(root, query, builder);
            };
        }

        static Specification<Category> byNameLike(String name) {
            return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
        }

        static Specification<Category> composeSpecification(String name, String orderBy, boolean ascending) {
            String orderByField = !StringUtils.hasText(orderBy)? "id" : orderBy;
            String nameFilter = !StringUtils.hasText(name)? "" : name;
            return orderBy(
                    byNameLike(nameFilter),
                    orderByField,
                    ascending
            );
        }

    }
}
