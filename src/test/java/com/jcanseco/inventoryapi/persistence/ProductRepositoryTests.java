package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.Objects;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UnitOfMeasurementRepository unitRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Sql("/multiple-products.sql")
    public void createProductShouldGenerateId() {

        var supplier = supplierRepository.findById(1L).orElseThrow();
        var category = categoryRepository.findById(1L).orElseThrow();
        var unit = unitRepository.findById(1L).orElseThrow();

        var product = newProduct(supplier, category, unit, "New Product", 19.99, 20.99);
        var newProduct = productRepository.saveAndFlush(product);

        assertNotNull(newProduct);
        assertTrue(newProduct.getId() > 0);
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsByNameContainingShouldReturnList() {
        var name = "l";
        var products = productRepository.findAll(
                ProductRepository.Specs.byNameContaining(name)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsBySupplierShouldReturnList() {
        var supplier = supplierRepository.findById(1L).orElseThrow();
        var products = productRepository.findAll(
                ProductRepository.Specs.bySupplier(supplier)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsByCategoryShouldReturnList() {
        var category = categoryRepository.findById(1L).orElseThrow();
        var products = productRepository.findAll(
                ProductRepository.Specs.byCategory(category)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsByUnitShouldReturnList() {
        var unit = unitRepository.findById(1L).orElseThrow();
        var products = productRepository.findAll(
                ProductRepository.Specs.byUnit(unit)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsByAllFiltersShouldReturnList() {
        var supplier = supplierRepository.findById(2L).orElseThrow();
        var category = categoryRepository.findById(2L).orElseThrow();
        var unit = unitRepository.findById(2L).orElseThrow();
        var products = productRepository.findAll(
                Objects.requireNonNull(ProductRepository.Specs.byAllFilters(
                        supplier,
                        category,
                        unit,
                        "s"
                ))
        );
        assertNotNull(products);
        assertEquals(6, products.size());
    }
}
