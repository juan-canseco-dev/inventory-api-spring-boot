package com.jcanseco.inventoryapi.persistence.products;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Create Products Persistence Tests")
@SpringBootTest
public class CreateProductPersistenceTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitOfMeasurementRepository unitRepository;

    @Autowired
    private ProductRepository productRepository;
    private Supplier supplier;
    private Category category;
    private UnitOfMeasurement unit;

    @Transactional
    @BeforeEach
    public void setup() {

        supplier = this.supplierRepository.saveAndFlush(
                newSupplier(
                        "ABC Corp",
                        "John Doe",
                        "555-1234-1",
                        newSupplierAddress(
                                "United States",
                                "California",
                                "San Francisco",
                                "94105",
                                "123 Main St"
                        )
                )
        );

        category = this.categoryRepository.saveAndFlush(
                newCategory("Electronics")
        );

        unit = this.unitRepository.saveAndFlush(
                newUnit("Piece")
        );
    }

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    public void createProductShouldGenerateId() {

        var product = newProduct(supplier, category, unit, "New Product", 19.99, 20.99);
        var newProduct = productRepository.saveAndFlush(product);

        assertNotNull(newProduct);
        assertTrue(newProduct.getId() > 0);
    }
}
