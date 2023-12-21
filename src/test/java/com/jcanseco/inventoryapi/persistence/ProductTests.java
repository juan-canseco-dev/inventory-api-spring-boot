package com.jcanseco.inventoryapi.persistence;

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
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Products Repository Tests")
@SpringBootTest
public class ProductTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitOfMeasurementRepository unitRepository;

    @Autowired
    private ProductRepository productRepository;

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

    private Category createCategory(String name) {
        return Category.builder()
                .name(name)
                .build();
    }

    private UnitOfMeasurement createUnit(String name) {
        return UnitOfMeasurement.builder()
                .name(name)
                .build();
    }

    private Product createProduct(Supplier supplier,
                                  Category category,
                                  UnitOfMeasurement unit,
                                  String name,
                                  Double purchasePrice,
                                  Double salePrice) {
        return Product.builder()
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(name)
                .purchasePrice(BigDecimal.valueOf(purchasePrice))
                .salePrice(BigDecimal.valueOf(salePrice))
                .build();
    }

    private Supplier supplier1;
    private Supplier supplier2;
    private Category category1;
    private Category category2;
    private UnitOfMeasurement unit1;
    private UnitOfMeasurement unit2;

    @Transactional
    @BeforeEach
    public void setup() {

        unit1 = this.unitRepository.saveAndFlush(
          createUnit("Piece")
        );

        unit2 = this.unitRepository.saveAndFlush(
          createUnit("Set")
        );

        category1 = this.categoryRepository.saveAndFlush(
                createCategory("Electronics")
        );

        category2 = this.categoryRepository.saveAndFlush(
                createCategory("Home and Garden")
        );

        supplier1 = this.supplierRepository.saveAndFlush(
                createSupplier(
                        "ABC Corp",
                        "John Doe",
                        "555-1234-1",
                        "United States",
                        "California",
                        "San Francisco",
                        "94105",
                        "123 Main St")
        );

        supplier2 = this.supplierRepository.saveAndFlush(
                createSupplier(
                        "Tech Solutions Inc",
                        "Alice Brown",
                        "555-1234-4",
                        "Australia",
                        "New South Wales",
                        "Sydney",
                        "2000",
                        "101 Tech Blvd"
                )
        );

        var products = List.of(
                createProduct(supplier1, category1, unit1, "Laptop",  800.00, 1200.00),
                createProduct(supplier1, category1, unit1,"Smartphone",  500.00, 800.00),
                createProduct(supplier1, category1, unit1,"HD Television",  600.00, 900.00),
                createProduct(supplier1, category1, unit1,"Tablet" ,200.00, 300.00),
                createProduct(supplier1, category1, unit1,"Digital Camera",  150.00, 250.00),
                createProduct(supplier1, category1, unit1,"Air Purifier",  80.00, 120.00),
                createProduct(supplier1, category1, unit1,"Refrigerator",  700.00, 1000.00),
                createProduct(supplier1, category1, unit1,"Bluetooth Speaker",  30.00, 50.00),
                createProduct(supplier1, category1, unit1,"Vacuum Cleaner", 90.00, 150.00),
                createProduct(supplier1, category1, unit1,"Toaster",  25.00, 40.00),
                createProduct(supplier2, category2, unit2,"Coffee Maker",  50.00, 100.00),
                createProduct(supplier2, category2, unit2,"Desk Chair",  100.00, 150.00),
                createProduct(supplier2, category2, unit2,"Washing Machine",  400.00, 700.00),
                createProduct(supplier2, category2, unit2,"Office Desk",  120.00, 200.00),
                createProduct(supplier2, category2, unit2,"Dinnerware Set",  60.00, 100.00),
                createProduct(supplier2, category2, unit2,"Bookshelf",  80.00, 120.00),
                createProduct(supplier2, category2, unit2,"Microwave Oven",  120.00, 200.00),
                createProduct(supplier2, category2, unit2,"Coffee Table",  60.00, 100.00),
                createProduct(supplier2, category2, unit2,"Vacuum Cleaner",  90.00, 150.00),
                createProduct(supplier2, category2, unit2,"Sleeping Bag",  40.00, 60.00)
        );

        productRepository.saveAllAndFlush(products);

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

        var product = Product.builder()
                .name("New Product")
                .supplier(supplier1)
                .category(category1)
                .unit(unit1)
                .purchasePrice(BigDecimal.valueOf(19.99))
                .salePrice(BigDecimal.valueOf(20.99))
                .build();

        var newProduct = productRepository.saveAndFlush(product);
        assertNotNull(newProduct);

        assertTrue(newProduct.getId() > 0);
    }

    @Test
    public void getProductsByNameContainingShouldReturnList() {
        var name = "l";
        var products = productRepository.findAll(
                ProductRepository.Specs.byNameContaining(name)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    public void getProductsBySupplierShouldReturnList() {
        var products = productRepository.findAll(
                ProductRepository.Specs.bySupplier(supplier1)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    public void getProductsByCategoryShouldReturnList() {
        var products = productRepository.findAll(
                ProductRepository.Specs.byCategory(category1)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    public void getProductsByUnitShouldReturnList() {
        var products = productRepository.findAll(
                ProductRepository.Specs.byUnit(unit1)
        );
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    public void getProductsByAllFiltersShouldReturnList() {
        var products = productRepository.findAll(
                Objects.requireNonNull(ProductRepository.Specs.byAllFilters(
                        supplier2,
                        category2,
                        unit2,
                        "s"
                ))
        );
        assertNotNull(products);
        assertEquals(6, products.size());
    }
}