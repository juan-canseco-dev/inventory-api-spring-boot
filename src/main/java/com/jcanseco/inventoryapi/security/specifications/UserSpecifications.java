package com.jcanseco.inventoryapi.security.specifications;

import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.entities.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> byFullNameLike(String fullName) {
        return (root, query, builder) -> builder.like(root.get("fullName"), "%" + fullName + "%");
    }

    public static Specification<User> byEmailLike(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> byEmail(String email) {
        return (root, query, builder) -> builder.equal(root.get("email"), email);
    }

    public static Specification<User> byRole(Role role) {
        return (root, query, builder) -> builder.equal(root.get("role"), role);
    }

    public static Specification<User> orderByIdAsc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<User> orderByIdDesc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("id"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }


    public static Specification<User> orderByRoleAsc(Specification<User> spec) {
        return (root, query, builder) -> {
            root.fetch("role");
            query.orderBy(
                    builder.asc(root.get("role").get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<User> orderByRoleDesc(Specification<User> spec) {
        return (root, query, builder) -> {
            root.fetch("role");
            query.orderBy(
                    builder.desc(root.get("role").get("name"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<User> orderByFullNameAsc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("fullName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }


    public static Specification<User> orderByFullNameDesc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("fullName"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<User> orderByEmailAsc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("email"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }


    public static Specification<User> orderByEmailDesc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("email"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<User> orderByCreatedAtAsc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("createdAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<User> orderByCreatedAtDesc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("createdAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    // Updated at
    public static Specification<User> orderByUpdatedAtAsc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(root.get("updatedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

    public static Specification<User> orderByUpdatedAtDesc(Specification<User> spec) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.desc(root.get("updatedAt"))
            );
            return spec.toPredicate(root, query, builder);
        };
    }

}
