package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import static com.jcanseco.inventoryapi.utils.TestModelFactory.newSupplier;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newSupplierAddress;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SupplierRepositoryTests {

    @Autowired
    private SupplierRepository repository;

    @Test
    public void createSupplierShouldGeneratedId() {

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
    @Sql("/multiple-suppliers.sql")
    public void findSuppliersByCompanyNameSpecificationShouldReturnList() {

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
    @Sql("/multiple-suppliers.sql")
    public void findSupplierByContactNameSpecificationShouldReturnList() {
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
    @Sql("/multiple-suppliers.sql")
    public void findSupplierByContactPhoneSpecificationShouldReturnList() {
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
    @Sql("/multiple-suppliers.sql")
    public void findSupplierByAllSpecificationsShouldReturnList() {

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
