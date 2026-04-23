package com.jcanseco.inventoryapi.catalog.units.persistence;

import com.jcanseco.inventoryapi.catalog.units.domain.UnitOfMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;




@Repository
public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurement, Long>,
        JpaSpecificationExecutor<UnitOfMeasurement> { }






