package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import com.jcanseco.inventoryapi.specifications.CustomerSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newCustomer;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newAddress;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void createCustomerShouldGenerateCustomerId() {

        var customer = newCustomer(
                "X1Y9Z3A7B2C8D6E0F5G4",
                "555-1234-1",
                "Smith Powell Johnson",
                newAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        var newCustomer = repository.saveAndFlush(customer);
        assertTrue(newCustomer.getId() > 0);
    }


    @Test
    public void createCustomerWithExistentDniShouldThrowException() {
        repository.saveAndFlush(
                newCustomer(
                        "901234567",
                        "555-1234-1",
                        "John Troll",
                        newAddress(
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
                newAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(newCustomer));
    }

    @Test
    @Sql("/multiple-customers.sql")
    public void findCustomersByDniLikeSpecificationShouldReturnList() {

        var dni = "12";
        var specification = CustomerSpecifications.byDniLike(dni);
        var customers = repository.findAll(specification);
        assertNotNull(customers);
        assertEquals(7, customers.size());
    }

    @Test
    @Sql("/multiple-customers.sql")
    public void findCustomersByPhoneLikeSpecificationShouldReturnList() {
        var phone = "1234-1";
        var specification = CustomerSpecifications.byPhoneLike(phone);
        var customers = repository.findAll(specification);
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    @Sql("/multiple-customers.sql")
    public void findCustomersByNameLikeSpecificationShouldReturnList() {
        var fullName = "john";
        var specification = CustomerSpecifications.byFullNameLike(fullName);
        var customers = repository.findAll(specification);
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }
}
