package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
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
        var specification = UnitOfMeasurementRepository.Specs.orderBy(
                UnitOfMeasurementRepository.Specs.byNameLike("gr"),
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

    @Test
    @Sql("/multiple-units_of_measurement.sql")
    public void findUnitsByComposeSpecificationWhenNameAndOrderByAreNotPresentShouldReturnList() {
        var firstUnitId = 1L;
        var spec = UnitOfMeasurementRepository.Specs.composeSpecification(
                "",
                "",
                true
        );
        var foundUnits = repository.findAll(spec);
        assertNotNull(foundUnits);
        assertEquals(foundUnits.size(), 10);
        var firstUnit = foundUnits.get(0);
        assertEquals(firstUnitId, firstUnit.getId());
    }



    @Test
    @Sql("/multiple-units_of_measurement.sql")
    public void composeSpecificationWhenNameIsNullOrEmptyShouldReturnList() {
        var firstUnitId = 10L;
        var spec = UnitOfMeasurementRepository.Specs.composeSpecification(
                "",
                "id",
                false
        );
        var foundUnits = repository.findAll(spec);
        assertNotNull(foundUnits);
        assertEquals(10, foundUnits.size());
        var firstUnit = foundUnits.get(0);
        assertEquals(firstUnitId, firstUnit.getId());
    }

    @Test
    @Sql("/multiple-units_of_measurement.sql")
    public void composeSpecificationWhenNameIsPresentShouldReturnExpectedList() {
        var spec = UnitOfMeasurementRepository.Specs.composeSpecification(
                "a",
                "name",
                true
        );
        var foundUnits = repository.findAll(spec);
        assertNotNull(foundUnits);
        assertEquals(3, foundUnits.size());
    }

}
