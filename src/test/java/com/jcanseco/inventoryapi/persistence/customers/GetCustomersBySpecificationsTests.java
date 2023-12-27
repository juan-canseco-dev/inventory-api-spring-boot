package com.jcanseco.inventoryapi.persistence.customers;

import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Get Customers By Specifications Persistence Tests")
@SpringBootTest

public class GetCustomersBySpecificationsTests {

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    public void setup() {
        var customers = List.of(
                newCustomer("123456789", "555-1234-1", "John Doe", newCustomerAddress("United States", "California", "San Francisco", "94105", "123 Main St")),
                newCustomer("987654321", "555-1234-2", "Jane Smith", newCustomerAddress("United Kingdom", "England", "London", "EC1A 1BB", "456 High St")),
                newCustomer("456789012", "555-1234-3", "Bob Johnson", newCustomerAddress("Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave")),
                newCustomer("789012345", "555-1234-4", "Alice Brown", newCustomerAddress("Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd")),
                newCustomer("234567890", "555-1234-5", "David Wilson", newCustomerAddress("Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse")),
                newCustomer("567890123", "555-1234-6", "Linda Miller", newCustomerAddress("Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave")),
                newCustomer("890123456", "555-1234-7", "Chris Taylor", newCustomerAddress("China", "Shanghai", "Shanghai", "200000", "456 Eco Street")),
                newCustomer("345678901", "555-1234-8", "Emily White", newCustomerAddress("South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road")),
                newCustomer("678901234", "555-1234-9", "Michael Lee", newCustomerAddress("India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane")),
                newCustomer("901234567", "555-1234-10", "Olivia Garcia", newCustomerAddress("New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View"))
        );
        repository.saveAllAndFlush(customers);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
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
