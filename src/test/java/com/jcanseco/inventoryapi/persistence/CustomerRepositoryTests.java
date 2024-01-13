package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import com.jcanseco.inventoryapi.specifications.CustomerSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repository;

    private Address address;

    @BeforeEach
    public void setup() {
        address =   Address.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    @Test
    public void createCustomerShouldGenerateCustomerId() {

        var customer = Customer.builder()
                .dni("X1Y9Z3A7B2C8D6E0F5G4")
                .fullName("Smith Powell Johnson")
                .phone("555-1234-1")
                .address(address)
                .build();

        var newCustomer = repository.saveAndFlush(customer);
        assertTrue(newCustomer.getId() > 0);
    }


    @Test
    public void createCustomerWithExistentDniShouldThrowException() {
        repository.saveAndFlush(

                Customer.builder()
                        .dni( "901234567")
                        .fullName("John Troll")
                        .phone("555-1234-1")
                        .address(address)
                        .build()
        );

        var newCustomer =   Customer.builder()
                .dni( "901234567")
                .fullName("John Smith Jr")
                .phone("555-1234-9")
                .address(address)
                .build();

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
