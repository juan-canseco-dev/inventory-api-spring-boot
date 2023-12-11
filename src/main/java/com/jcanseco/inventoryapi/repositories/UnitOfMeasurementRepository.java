package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurement, Long> {
    List<UnitOfMeasurement> findAllByNameContainingOrderByName(String name, Sort sort);
    Page<UnitOfMeasurement> findAllByNameContainingOrderByName(String name, Pageable pageable);
}
