package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>
{
    List<Supplier> findAllByCompanyNameContainingOrContactNameContainingOrContactPhoneContaining(
            String companyName,
            String contactName,
            String contactPhone,
            Sort sort
    );

    Page<Supplier> findAllByCompanyNameContainingOrContactNameContainingOrContactPhoneContaining(
            String companyName,
            String contactName,
            String contactPhone,
            Pageable pageable
    );
}
