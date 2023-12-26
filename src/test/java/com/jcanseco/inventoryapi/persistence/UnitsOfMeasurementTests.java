package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Units Of Measurement Tests")
@SpringBootTest
public class UnitsOfMeasurementTests {

    @Autowired
    private UnitOfMeasurementRepository repository;

    @BeforeEach
    public void setup() {
        var units = List.of(
                newUnit("Meter"),
                newUnit("Kilogram"),
                newUnit("Liter"),
                newUnit("Gram"),
                newUnit("Millimeter"),
                newUnit("Centimeter"),
                newUnit("Inch"),
                newUnit("Pound"),
                newUnit("Gallon"),
                newUnit("Ounce")
        );
        repository.saveAllAndFlush(units);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createUnitWhenValidUnitReturnSavedUnitWithGeneratedId() {

        var unit = newUnit("New Unit");
        var newUnit = repository.saveAndFlush(unit);

        assertTrue(newUnit.getId() > 0);
        assertEquals(unit.getName(), newUnit.getName());

        var unitOtp = repository.findById(newUnit.getId());
        assertTrue(unitOtp.isPresent());
    }

    @Test
    public void findAllUnitsByNameWhenNameIsEmptyShouldReturnList() {
        var foundUnits = repository.findAllByNameContaining("", Sort.by("name").ascending());
        assertNotNull(foundUnits);
        assertEquals(10, foundUnits.size());
    }

    @Test
    public void findAllUnitsByNameContainingWhenContains() {
        var foundUnits = repository.findAllByNameContaining("oun", Sort.by("name").ascending());
        assertEquals(2, foundUnits.size());
    }
}
