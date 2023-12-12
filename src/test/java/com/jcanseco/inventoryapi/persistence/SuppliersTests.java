package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Suppliers Repository Tests")
@SpringBootTest
public class SuppliersTests {

    @Autowired
    private SupplierRepository repository;


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
    public void findAllSuppliersByAllSearchFiltersShouldReturnList() {

        var companyName = "p";
        var contactName = "m";
        var contactPhone = "9";

        var foundSuppliers = this.repository.findAllByCompanyNameContainingOrContactNameContainingOrContactPhoneContaining(
                companyName,
                contactName,
                contactPhone,
                Sort.by("companyName").ascending()
        );

        assertNotNull(foundSuppliers);
        assertEquals(6, foundSuppliers.size());
    }

    @Test
    public void findSuppliersPageByAllSearchFiltersShouldPagedReturnList() {

        var companyName = "p";
        var contactName = "m";
        var contactPhone = "9";

        var sort = Sort.by("companyName").ascending();

        var pageRequest = PageRequest.of(0, 3, sort);

        var page = this.repository.findAllByCompanyNameContainingOrContactNameContainingOrContactPhoneContaining(
                companyName,
                contactName,
                contactPhone,
                pageRequest
        );

        assertNotNull(page.getContent());
        assertEquals(3, page.getContent().size());
        assertEquals(2, page.getTotalPages());
        assertEquals(6, page.getTotalElements());
    }
}
