package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.specifications.SupplierSpecifications;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Suppliers Repository Tests")
@SpringBootTest
public class SuppliersTests {

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private SupplierSpecifications specifications;


    private Supplier createSupplier(String companyName,
                                    String contactName,
                                    String contactPhone,
                                    String country,
                                    String state,
                                    String city,
                                    String zipCode,
                                    String street) {
        return Supplier.builder()
                .companyName(companyName)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .address(SupplierAddress.builder()
                        .country(country)
                        .state(state)
                        .city(city)
                        .zipCode(zipCode)
                        .street(street)
                        .build())
                .build();
    }


    @BeforeEach
    public void setup() {
        var suppliers = List.of(
                createSupplier("ABC Corp", "John Doe", "555-1234-1", "United States", "California", "San Francisco", "94105", "123 Main St"),
                createSupplier("XYZ Ltd", "Jane Smith", "555-1234-2", "United Kingdom", "England", "London", "EC1A 1BB", "456 High St"),
                createSupplier("123 Enterprises", "Bob Johnson", "555-1234-3", "Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave"),
                createSupplier("Tech Solutions Inc", "Alice Brown", "555-1234-4", "Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd"),
                createSupplier("Global Innovations", "David Wilson", "555-1234-5", "Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse"),
                createSupplier("Sunshine Foods", "Linda Miller", "555-1234-6", "Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave"),
                createSupplier("Green Energy Co", "Chris Taylor", "555-1234-7", "China", "Shanghai", "Shanghai", "200000", "456 Eco Street"),
                createSupplier("Peak Performance", "Emily White", "555-1234-8", "South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road"),
                createSupplier("City Builders", "Michael Lee", "555-1234-9", "India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane"),
                createSupplier("Oceanic Ventures", "Olivia Garcia", "555-1234-10", "New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View")
        );
        repository.saveAllAndFlush(suppliers);
    }

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
                .street("Center")
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

    @Test
    public void findSuppliersByCompanyNameShouldReturnList() {

        var companyName = "a";
        var contactName = "";
        var contactPhone = "";

        var sort = Sort.by("companyName").ascending();

        var criteria = specifications.getSupplierSpecification(
                companyName,
                contactName,
                contactPhone
        );

        assertNotNull(criteria);
        var foundSuppliers = repository.findAll(criteria, sort);

        assertNotNull(foundSuppliers);
        assertEquals(4, foundSuppliers.size());
    }

    @Test
    public void findSupplierByContactNameShouldReturnList() {
        var companyName = "";
        var contactName = "j";
        var contactPhone = "";

        var sort = Sort.by("companyName").ascending();

        var criteria = specifications.getSupplierSpecification(
                companyName,
                contactName,
                contactPhone
        );

        assertNotNull(criteria);

        var foundSuppliers = repository.findAll(criteria, sort);

        assertNotNull(foundSuppliers);
        assertEquals(3, foundSuppliers.size());
    }

    @Test
    public void findSupplierByContactPhoneShouldReturnList() {
        var companyName = "";
        var contactName = "";
        var contactPhone = "9";

        var sort = Sort.by("companyName").ascending();

        var criteria = specifications.getSupplierSpecification(
                companyName,
                contactName,
                contactPhone
        );

        assertNotNull(criteria);

        var foundSuppliers = repository.findAll(criteria, sort);

        assertNotNull(foundSuppliers);
        assertEquals(1, foundSuppliers.size());
    }

    @Test
    public void findSupplierByAllFiltersShouldReturnList() {

        var companyName = "a";
        var contactName = "l";
        var contactPhone = "5";

        var sort = Sort.by("companyName").ascending();

        var criteria = specifications.getSupplierSpecification(
                companyName,
                contactName,
                contactPhone
        );

        assertNotNull(criteria);

        var foundSuppliers = repository.findAll(criteria, sort);

        assertNotNull(foundSuppliers);
        assertEquals(3, foundSuppliers.size());

    }
}
