package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Units Of Measurement Tests")
@SpringBootTest
public class UnitsOfMeasurementTests {

    @Autowired
    private UnitOfMeasurementRepository repository;

    @BeforeEach
    public void setup() {
        var units = List.of(
                createUnit("Meter"),
                createUnit("Kilogram"),
                createUnit("Liter"),
                createUnit("Gram"),
                createUnit("Millimeter"),
                createUnit("Centimeter"),
                createUnit("Inch"),
                createUnit("Pound"),
                createUnit("Gallon"),
                createUnit("Ounce")
        );
        repository.saveAllAndFlush(units);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    private UnitOfMeasurement createUnit(String name) {
        return UnitOfMeasurement.builder()
                .name(name)
                .build();
    }
    @Test
    public void createUnitWhenValidUnitReturnSavedUnitWithGeneratedId() {

        var unit = createUnit("New Unit");
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

    @Test
    public void findAllPagedUnitsByNameWhenInputIsValidShouldReturnValidPage() {
        var request = PageRequest.of(0, 2);
        var page = repository.findAllByNameContaining("l", request);

        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size());
        assertEquals(2, page.getTotalPages());
        assertEquals(4, page.getTotalElements());
    }

    @Test
    public void findAllPagedUnitsWhenNameIsEmptyShouldReturnValidPageWithAllItems() {
        var request = PageRequest.of(0, 10);
        var page = repository.findAllByNameContaining("", request);

        assertNotNull(page.getContent());
        assertEquals(10, page.getContent().size());
    }

}
