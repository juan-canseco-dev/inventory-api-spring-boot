package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.CustomerAddress;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Customers Repository Tests")
@SpringBootTest
public class CustomerTests {

    @Autowired
    private CustomerRepository repository;

    private Customer createCustomer(String dni, String phone, String fullName, CustomerAddress address) {
        return Customer.builder()
                .dni(dni)
                .phone(phone)
                .fullName(fullName)
                .address(address)
                .build();
    }

    private CustomerAddress createCustomerAddress(String country, String state, String city, String zipCode, String street) {
        return CustomerAddress.builder()
                .country(country)
                .state(state)
                .city(city)
                .zipCode(zipCode)
                .street(street)
                .build();
    }

    @BeforeEach
    public void setup() {
        var customers = List.of(
                createCustomer("123456789", "555-1234-1", "John Doe", createCustomerAddress("United States", "California", "San Francisco", "94105", "123 Main St")),
                createCustomer("987654321", "555-1234-2", "Jane Smith", createCustomerAddress("United Kingdom", "England", "London", "EC1A 1BB", "456 High St")),
                createCustomer("456789012", "555-1234-3", "Bob Johnson", createCustomerAddress("Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave")),
                createCustomer("789012345", "555-1234-4", "Alice Brown", createCustomerAddress("Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd")),
                createCustomer("234567890", "555-1234-5", "David Wilson", createCustomerAddress("Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse")),
                createCustomer("567890123", "555-1234-6", "Linda Miller", createCustomerAddress("Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave")),
                createCustomer("890123456", "555-1234-7", "Chris Taylor", createCustomerAddress("China", "Shanghai", "Shanghai", "200000", "456 Eco Street")),
                createCustomer("345678901", "555-1234-8", "Emily White", createCustomerAddress("South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road")),
                createCustomer("678901234", "555-1234-9", "Michael Lee", createCustomerAddress("India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane")),
                createCustomer("901234567", "555-1234-10", "Olivia Garcia", createCustomerAddress("New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View"))
        );
        repository.saveAllAndFlush(customers);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }


    @Test
    public void createCustomerShouldGenerateCustomerId() {

        var address = CustomerAddress
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();


        var customer = Customer
                .builder()
                .dni("X1Y9Z3A7B2C8D6E0F5G4")
                .phone("555-1234-1")
                .fullName("Smith Powell Johnson")
                .address(address)
                .build();

        var newCustomer = repository.saveAndFlush(customer);
        assertTrue(newCustomer.getId() > 0);
        assertNotNull(newCustomer.getAddress());
        assertTrue(newCustomer.getAddress().getId() > 0);
    }


    @Test
    public void createCustomerWithExistentDniShouldThrowException() {
        var address = CustomerAddress
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();


        var customer = Customer
                .builder()
                .dni("901234567")
                .phone("555-1234-1")
                .fullName("John Troll")
                .address(address)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(customer));
    }

    @Test
    public void findCustomersWithGetByDniContainingSpecificationShouldReturnList() {

        var dni = "12";
        var specification = CustomerRepository.Specs.byDniContaining(dni);
        var customers = repository.findAll(Specification.where(specification));
        assertNotNull(customers);
        assertEquals(7, customers.size());
    }

    @Test
    public void findCustomersWithGetByPhoneContainingSpecificationShouldReturnList() {
        var phone = "1234-1";
        var specification = CustomerRepository.Specs.byPhoneContaining(phone);
        var customers = repository.findAll(Specification.where(specification));
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    public void findCustomersWithGetByFullNameContainingSpecificationShouldReturnList() {
        var fullName = "john";
        var specification = CustomerRepository.Specs.byFullNameContaining(fullName);
        var customers = repository.findAll(Specification.where(specification));
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    public void findCustomersByDniOrPhoneOrFUllNameSpecificationShouldReturnList() {
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
