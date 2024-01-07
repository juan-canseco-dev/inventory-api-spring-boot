package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.specifications.UnitOfMeasurementSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UnitOfMeasurementRepositoryTests {

    @Autowired
    private UnitOfMeasurementRepository repository;

    @Test
    public void createUnitShouldGeneratedId() {

        var unit = newUnit("New Unit");
        var newUnit = repository.saveAndFlush(unit);

        assertTrue(newUnit.getId() > 0);
        assertEquals(unit.getName(), newUnit.getName());

        var unitOtp = repository.findById(newUnit.getId());
        assertTrue(unitOtp.isPresent());
    }

    @Test
    @Sql("/multiple-units_of_measurement.sql")
    public void findUnitsByNameLikeSpecificationShouldReturnList() {
        var expectedUnitName = "Gram";
        var specification = UnitOfMeasurementSpecifications.orderBy(
                UnitOfMeasurementSpecifications.byNameLike("gr"),
                "name",
                true
        );
        var foundUnits = repository.findAll(specification);
        assertNotNull(foundUnits);
        assertFalse(foundUnits.isEmpty());
        var firstUnit = foundUnits.get(0);
        assertEquals(expectedUnitName, firstUnit.getName());
        assertEquals(2, foundUnits.size());
    }

}
