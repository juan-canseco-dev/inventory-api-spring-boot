package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.specifications.SupplierSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;
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
                newAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        var newSupplier = repository.saveAndFlush(supplier);
        assertTrue(newSupplier.getId() > 0);
    }

    @Test
    @Sql("/multiple-suppliers.sql")
    public void findSuppliersByCompanyNameLikeSpecificationShouldReturnList() {
        var specification = SupplierSpecifications.orderBy(
                SupplierSpecifications.byCompanyNameLike("a"),
                "id",
                true
        );
        var foundSuppliers = repository.findAll(specification);
        assertNotNull(foundSuppliers);
        assertEquals(4, foundSuppliers.size());
    }

    @Test
    @Sql("/multiple-suppliers.sql")
    public void findSuppliersByContactNameLikeSpecificationShouldReturnList() {
        var specification = SupplierSpecifications.orderBy(
                SupplierSpecifications.byContactNameLike("j"),
                "id",
                true
        );
        var foundSuppliers = repository.findAll(specification);
        assertNotNull(foundSuppliers);
        assertEquals(3, foundSuppliers.size());
    }

    @Test
    @Sql("/multiple-suppliers.sql")
    public void findSuppliersByContactPhoneLikeSpecificationShouldReturnList() {
        var specification = SupplierSpecifications.orderBy(
                SupplierSpecifications.byContactPhoneLike("9"),
                "id",
                true
        );
        var foundSuppliers = repository.findAll(specification);
        assertNotNull(foundSuppliers);
        assertEquals(1, foundSuppliers.size());
    }
}
