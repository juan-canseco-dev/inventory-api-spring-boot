package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Units Of Measurement Tests")
@SpringBootTest
public class UnitsOfMeasurementTests {
    @Autowired
    private UnitOfMeasurementRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createUnitWhenValidUnitReturnSavedUnitWithGeneratedId() {

        var unit = UnitOfMeasurement.builder().name("Electronics").build();
        var newUnit = repository.saveAndFlush(unit);

        assertTrue(newUnit.getId() > 0);
        assertEquals(unit.getName(), newUnit.getName());

        var categoryOpt = repository.findById(newUnit.getId());
        assertTrue(categoryOpt.isPresent());

    }

    @Test
    public void findAllUnitsByNameWhenNameIsEmptyShouldReturnList() {

        var units = List.of(
                UnitOfMeasurement.builder().name("Kilogram").build(),
                UnitOfMeasurement.builder().name("Box").build()
        );

        var savedUnits = this.repository.saveAllAndFlush(units);
        var foundUnits = repository.findAllByNameContainingOrderByName("");

        assertTrue(
                savedUnits.size() == foundUnits.size() &&
                        savedUnits.containsAll(foundUnits) &&
                        foundUnits.containsAll(savedUnits)
        );
    }

    @Test
    public void findAllUnitsByNameContainingWhenContains() {
        var units = List.of(
                UnitOfMeasurement.builder().name("Kilogram").build(),
                UnitOfMeasurement.builder().name("Box").build()
        );
        this.repository.saveAllAndFlush(units);
        var foundUnits = repository.findAllByNameContainingOrderByName("kilo");
        assertEquals(1, foundUnits.size());
    }

    @Test
    public void findAllPagedUnitsByNameWhenInputIsValidShouldReturnValidPage() {

        var units = List.of(
                UnitOfMeasurement.builder().name("Box").build(),
                UnitOfMeasurement.builder().name("Kilogram").build(),
                UnitOfMeasurement.builder().name("Kilo").build()
        );

        repository.saveAllAndFlush(units);

        var request = PageRequest.of(0, 10);
        var page = repository.findAllByNameContainingOrderByName("ki", request);

        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size());
    }

    @Test
    public void findAllPagedUnitsWhenNameIsEmptyShouldReturnValidPageWithAllItems() {

        var units = List.of(
                UnitOfMeasurement.builder().name("Box").build(),
                UnitOfMeasurement.builder().name("Kilo").build(),
                UnitOfMeasurement.builder().name("Kilogram").build()
        );

        repository.saveAllAndFlush(units);

        var request = PageRequest.of(0, 10);
        var page = repository.findAllByNameContainingOrderByName("", request);

        assertNotNull(page.getContent());
        assertEquals(3, page.getContent().size());
    }

}
