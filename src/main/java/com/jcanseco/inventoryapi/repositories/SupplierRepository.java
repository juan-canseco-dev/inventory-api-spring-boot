package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Supplier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    interface Specs {
        static Specification<Supplier> byCompanyNameContaining(String companyName) {
            return (root, query, builder) -> builder.like(root.get("companyName"), "%" + companyName + "%");
        }

        static Specification<Supplier> byContactNameContaining(String contactName) {
            return (root, query, builder) -> builder.like(root.get("contactName"), "%" + contactName + "%");
        }
        static Specification<Supplier> byContactPhoneContaining(String contactPhone) {
            return (root, query, builder) -> builder.like(root.get("contactPhone"), "%" + contactPhone + "%");
        }
        @Nullable
        static Specification<Supplier> byCompanyAndContactInfo(String companyName, String contactName, String contactPhone) {
            var specs = new ArrayList<Specification<Supplier>>();
            Optional.ofNullable(companyName).filter(StringUtils::hasText).ifPresent(name -> specs.add(byCompanyNameContaining(name)));
            Optional.ofNullable(contactName).filter(StringUtils::hasText).ifPresent(name -> specs.add(byContactNameContaining(name)));
            Optional.ofNullable(contactPhone).filter(StringUtils::hasText).ifPresent(phone -> specs.add(byContactPhoneContaining(phone)));
            return specs.isEmpty()? null : specs.stream().reduce(Specification::and).orElse(null);
        }
    }
}