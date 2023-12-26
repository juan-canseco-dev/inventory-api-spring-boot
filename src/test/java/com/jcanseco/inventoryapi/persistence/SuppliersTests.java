package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Suppliers Repository Tests")
@SpringBootTest
public class SuppliersTests {

    @Autowired
    private SupplierRepository repository;
    @BeforeEach
    public void setup() {
        var suppliers = List.of(
                newSupplier("ABC Corp", "John Doe", "555-1234-1", newSupplierAddress("United States", "California", "San Francisco", "94105", "123 Main St")),
                newSupplier("XYZ Ltd", "Jane Smith", "555-1234-2", newSupplierAddress("United Kingdom", "England", "London", "EC1A 1BB", "456 High St")),
                newSupplier("123 Enterprises", "Bob Johnson", "555-1234-3", newSupplierAddress("Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave")),
                newSupplier("Tech Solutions Inc", "Alice Brown", "555-1234-4", newSupplierAddress("Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd")),
                newSupplier("Global Innovations", "David Wilson", "555-1234-5", newSupplierAddress("Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse")),
                newSupplier("Sunshine Foods", "Linda Miller", "555-1234-6", newSupplierAddress("Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave")),
                newSupplier("Green Energy Co", "Chris Taylor", "555-1234-7", newSupplierAddress("China", "Shanghai", "Shanghai", "200000", "456 Eco Street")),
                newSupplier("Peak Performance", "Emily White", "555-1234-8", newSupplierAddress("South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road")),
                newSupplier("City Builders", "Michael Lee", "555-1234-9", newSupplierAddress("India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane")),
                newSupplier("Oceanic Ventures", "Olivia Garcia", "555-1234-10", newSupplierAddress("New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View"))
        );
        repository.saveAllAndFlush(suppliers);
    }

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

    @Test
    public void findSuppliersByCompanyNameShouldReturnList() {

        var companyName = "a";
        var contactName = "";
        var contactPhone = "";

        var sort = Sort.by("companyName").ascending();

        var criteria = SupplierRepository.Specs.byCompanyAndContactInfo(
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

        var criteria = SupplierRepository.Specs.byCompanyAndContactInfo(
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

        var criteria = SupplierRepository.Specs.byCompanyAndContactInfo(
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

        var criteria = SupplierRepository.Specs.byCompanyAndContactInfo(
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
