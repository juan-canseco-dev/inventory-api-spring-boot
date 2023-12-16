package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Customer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    interface Specs {
        static Specification<Customer> byDniContaining(String dni) {
            return (root, query, builder) -> builder.like(root.get("dni"), "%" + dni + "%");
        }

        static Specification<Customer> byPhoneContaining(String phone) {
            return (root, query, builder) -> builder.like(root.get("phone"), "%" + phone + "%");
        }

        static Specification<Customer> byFullNameContaining(String fullName) {
            return (root, query, builder) -> builder.like(root.get("fullName"), "%" + fullName + "%");
        }

        @Nullable
        static Specification<Customer> byDniOrPhoneOrFullName(String dni, String phone, String fullName) {
            var specs = new ArrayList<Specification<Customer>>();
            Optional.ofNullable(dni).filter(StringUtils::hasText).ifPresent(f -> specs.add(byDniContaining(f)));
            Optional.ofNullable(phone).filter(StringUtils::hasText).ifPresent(f -> specs.add(byPhoneContaining(f)));
            Optional.ofNullable(fullName).filter(StringUtils::hasText).ifPresent(f -> specs.add(byFullNameContaining(f)));
            return specs.isEmpty()? null : specs.stream().reduce(Specification::and).orElse(null);
        }
    }
}
