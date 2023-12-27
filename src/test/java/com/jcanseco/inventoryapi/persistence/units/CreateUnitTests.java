package com.jcanseco.inventoryapi.persistence.units;

import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Create Unit Of Measurement Persistence Tests")
@SpringBootTest
public class CreateUnitTests {

    @Autowired
    private UnitOfMeasurementRepository repository;

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
}
