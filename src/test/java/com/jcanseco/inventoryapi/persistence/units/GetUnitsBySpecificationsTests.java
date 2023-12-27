package com.jcanseco.inventoryapi.persistence.units;

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

@DisplayName("Get Units Of Measurement By Specifications Persistence Tests")
@SpringBootTest
public class GetUnitsBySpecificationsTests {

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
