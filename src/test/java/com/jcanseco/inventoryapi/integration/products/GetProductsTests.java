package com.jcanseco.inventoryapi.integration.products;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newProduct;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class GetProductsTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitOfMeasurementRepository unitRepository;

    @Autowired
    private ProductRepository productRepository;

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
                newUnit("Piece")
        );

        unit2 = this.unitRepository.saveAndFlush(
                newUnit("Set")
        );

        category1 = this.categoryRepository.saveAndFlush(
                newCategory("Electronics")
        );

        category2 = this.categoryRepository.saveAndFlush(
                newCategory("Home and Garden")
        );

        supplier1 = this.supplierRepository.saveAndFlush(
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
                        ))
        );

        supplier2 = this.supplierRepository.saveAndFlush(
                newSupplier(
                        "Tech Solutions Inc",
                        "Alice Brown",
                        "555-1234-4",

                        newSupplierAddress("Australia",
                                "New South Wales",
                                "Sydney",
                                "2000",
                                "101 Tech Blvd"
                        ))
        );

        var products = List.of(
                newProduct(supplier1, category1, unit1, "Laptop", 800.00, 1200.00),
                newProduct(supplier1, category1, unit1, "Smartphone", 500.00, 800.00),
                newProduct(supplier1, category1, unit1, "HD Television", 600.00, 900.00),
                newProduct(supplier1, category1, unit1, "Tablet", 200.00, 300.00),
                newProduct(supplier1, category1, unit1, "Digital Camera", 150.00, 250.00),
                newProduct(supplier1, category1, unit1, "Air Purifier", 80.00, 120.00),
                newProduct(supplier1, category1, unit1, "Refrigerator", 700.00, 1000.00),
                newProduct(supplier1, category1, unit1, "Bluetooth Speaker", 30.00, 50.00),
                newProduct(supplier1, category1, unit1, "Vacuum Cleaner", 90.00, 150.00),
                newProduct(supplier1, category1, unit1, "Toaster", 25.00, 40.00),
                newProduct(supplier2, category2, unit2, "Coffee Maker", 50.00, 100.00),
                newProduct(supplier2, category2, unit2, "Desk Chair", 100.00, 150.00),
                newProduct(supplier2, category2, unit2, "Washing Machine", 400.00, 700.00),
                newProduct(supplier2, category2, unit2, "Office Desk", 120.00, 200.00),
                newProduct(supplier2, category2, unit2, "Dinnerware Set", 60.00, 100.00),
                newProduct(supplier2, category2, unit2, "Bookshelf", 80.00, 120.00),
                newProduct(supplier2, category2, unit2, "Microwave Oven", 120.00, 200.00),
                newProduct(supplier2, category2, unit2, "Coffee Table", 60.00, 100.00),
                newProduct(supplier2, category2, unit2, "Vacuum Cleaner", 90.00, 150.00),
                newProduct(supplier2, category2, unit2, "Sleeping Bag", 40.00, 60.00)
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
    public void getProductsWithEmptyFiltersShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(20))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getProductsByNameShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "l");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getProductsBySupplierShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("supplierId", supplier1.getId().toString());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getProductsByCategoryShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("categoryId", category1.getId().toString());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void getProductsByUnitShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("unitId", unit1.getId().toString());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void getProductsByAllFiltersShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("supplierId", supplier2.getId().toString())
                .param("categoryId", category2.getId().toString())
                .param("unitId", unit2.getId().toString())
                .param("name", "s");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(6))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void getProductsWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("orderBy", "invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getProductsByInvalidSortOrderStatusShouldBeBadRequest() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortOrder", "invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
