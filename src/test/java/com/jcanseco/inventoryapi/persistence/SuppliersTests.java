package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Suppliers Repository Tests")
@SpringBootTest
public class SuppliersTests {

    @Autowired
    private SupplierRepository repository;

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }


    @Test
    public void createSupplierWhenValidSupplierShouldReturnSavedSupplierWithGeneratedId() {

        var address = SupplierAddress
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .line1("Center")
                .build();

        var supplier = Supplier
                .builder()
                .companyName("ABC Electronics")
                .contactName("John Doe")
                .contactPhone("+12222222")
                .address(address)
                .build();

        var newSupplier = repository.saveAndFlush(supplier);
        assertTrue(newSupplier.getId() > 0);
        assertNotNull(newSupplier.getAddress());
        assertTrue(newSupplier.getAddress().getId() > 0);
    }

}
