package com.jcanseco.inventoryapi.persistence.customers;

import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Create Customer Persistence Tests")
@SpringBootTest
public class CreateCustomerTests {

    @Autowired
    private CustomerRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createCustomerShouldGenerateCustomerId() {

        var customer = newCustomer(
                "X1Y9Z3A7B2C8D6E0F5G4",
                "555-1234-1",
                "Smith Powell Johnson",
                newCustomerAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        var newCustomer = repository.saveAndFlush(customer);
        assertTrue(newCustomer.getId() > 0);
        assertNotNull(newCustomer.getAddress());
        assertTrue(newCustomer.getAddress().getId() > 0);
    }


    @Test
    public void createCustomerWithExistentDniShouldThrowException() {
        repository.saveAndFlush(
                newCustomer(
                        "901234567",
                        "555-1234-1",
                        "John Troll",
                        newCustomerAddress(
                                "Mexico",
                                "Sonora",
                                "Hermosillo",
                                "83200",
                                "Center"
                        )
                )
        );

        var newCustomer = newCustomer(
                "901234567",
                "555-1234-9",
                "Jane Troll",
                newCustomerAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(newCustomer));
    }

}
