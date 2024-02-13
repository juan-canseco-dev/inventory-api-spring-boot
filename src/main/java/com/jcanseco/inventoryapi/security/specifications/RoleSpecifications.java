package com.jcanseco.inventoryapi.security.specifications;

import com.jcanseco.inventoryapi.security.entities.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecifications {
    public static Specification<Role> byNameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Role> byName(String name) {
        return (root, query, builder) -> builder.equal(root.get("name"), name);
    }


    public static Specification<Role> orderByIdAsc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Role> orderByIdDesc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }


    public static Specification<Role> orderByNameAsc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }


    public static Specification<Role> orderByNameDesc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Role> orderByCreatedAtAsc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("createdAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Role> orderByCreatedAtDesc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("createdAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    // Updated at
    public static Specification<Role> orderByUpdatedAtAsc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("updatedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<Role> orderByUpdatedAtDesc(Specification<Role> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("updatedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

}
