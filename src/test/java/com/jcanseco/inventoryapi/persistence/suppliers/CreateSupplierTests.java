package com.jcanseco.inventoryapi.persistence.suppliers;

import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Create Supplier Persistence Tests")
@SpringBootTest
public class CreateSupplierTests {

    @Autowired
    private SupplierRepository repository;

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void createSupplierWhenValidSupplierShouldReturnSavedSupplierWithGeneratedId() {

        var supplier = newSupplier(
                "ABC Electronics",
                "John Doe",
                "+12222222",
                newSupplierAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        var newSupplier = repository.saveAndFlush(supplier);
        assertTrue(newSupplier.getId() > 0);
        assertNotNull(newSupplier.getAddress());
        assertTrue(newSupplier.getAddress().getId() > 0);
    }
}
