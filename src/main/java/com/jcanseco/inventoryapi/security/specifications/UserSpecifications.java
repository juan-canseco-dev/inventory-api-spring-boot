package com.jcanseco.inventoryapi.security.specifications;

import com.jcanseco.inventoryapi.security.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> byEmail(String email) {
        return (root, query, builder) -> builder.equal(root.get("email"), email);
    }
}
