package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.Supplier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class SupplierSpecifications {
    static Specification<Supplier> companyNameContains(String companyName) {
        return (supplier, cq, cb) -> cb.like(supplier.get("companyName"), "%" + companyName + "%");
    }

    static Specification<Supplier> contactNameContains(String contactName) {
        return (supplier, cq, cb) -> cb.like(supplier.get("contactName"), "%" + contactName + "%");
    }

    static Specification<Supplier> contactPhoneContains(String contactPhone) {
        return (supplier, cq, cb) -> cb.like(supplier.get("contactPhone"), "%" + contactPhone + "%");
    }

    @Nullable
    public Specification<Supplier> getSupplierSpecification(String companyName, String contactName, String contactPhone) {
        var specs = new ArrayList<Specification<Supplier>>();
        Optional.ofNullable(companyName).filter(StringUtils::hasText).ifPresent(name -> specs.add(companyNameContains(name)));
        Optional.ofNullable(contactName).filter(StringUtils::hasText).ifPresent(name -> specs.add(contactNameContains(name)));
        Optional.ofNullable(contactPhone).filter(StringUtils::hasText).ifPresent(phone -> specs.add(contactPhoneContains(phone)));
        return specs.isEmpty()? null : specs.stream().reduce(Specification::and).orElse(null);
    }

}
