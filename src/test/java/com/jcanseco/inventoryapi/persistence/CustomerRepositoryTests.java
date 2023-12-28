package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newCustomer;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newCustomerAddress;
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

    @Test
    @Sql("/multiple-customers.sql")
    public void findCustomersByDniContainingSpecificationShouldReturnList() {

        var dni = "12";
        var specification = CustomerRepository.Specs.byDniContaining(dni);
        var customers = repository.findAll(Specification.where(specification));
        assertNotNull(customers);
        assertEquals(7, customers.size());
    }

    @Test
    @Sql("/multiple-customers.sql")
    public void findCustomersByPhoneContainingSpecificationShouldReturnList() {
        var phone = "1234-1";
        var specification = CustomerRepository.Specs.byPhoneContaining(phone);
        var customers = repository.findAll(Specification.where(specification));
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    @Sql("/multiple-customers.sql")
    public void findCustomersByFullNameContainingSpecificationShouldReturnList() {
        var fullName = "john";
        var specification = CustomerRepository.Specs.byFullNameContaining(fullName);
        var customers = repository.findAll(Specification.where(specification));
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    @Sql("/multiple-customers.sql")
    public void findCustomersByAllSpecificationsShouldReturnList() {
        var dni = "1";
        var phone = "555";
        var fullName = "o";
        var specification = CustomerRepository.Specs.byDniOrPhoneOrFullName(
                dni,
                phone,
                fullName
        );
        var customers = repository.findAll(Specification.where(specification));
        assertNotNull(customers);
        assertEquals(5, customers.size());
    }
}
