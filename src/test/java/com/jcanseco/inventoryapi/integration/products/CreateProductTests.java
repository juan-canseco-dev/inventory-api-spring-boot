package com.jcanseco.inventoryapi.integration.products;

import com.jcanseco.inventoryapi.dtos.products.CreateProductDto;
import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateProductTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UnitOfMeasurementRepository unitRepository;
    @Autowired
    private ProductRepository productRepository;
    private Supplier savedSupplier;
    private Category savedCategory;
    private UnitOfMeasurement savedUnit;

    @BeforeEach
    public void setup() {

        var address = Address
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var supplier = Supplier.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var category = Category.builder()
                .name("Electronics")
                .build();

        var unit = UnitOfMeasurement.builder()
                .name("Piece")
                .build();

        savedSupplier = supplierRepository.saveAndFlush(supplier);
        savedCategory = categoryRepository.saveAndFlush(category);
        savedUnit = unitRepository.saveAndFlush(unit);
    }

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    public void createProductWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var createDto = CreateProductDto.builder()
                .name("Laptop")
                .supplierId(savedSupplier.getId())
                .categoryId(savedCategory.getId())
                .unitId(savedUnit.getId())
                .purchasePrice(10.99)
                .salePrice(20.99)
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void createProductWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var createDto = CreateProductDto.builder()
                .name("Laptop")
                .categoryId(savedCategory.getId())
                .unitId(savedUnit.getId())
                .purchasePrice(10.99)
                .salePrice(20.99)
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductWhenSupplierNotExistsStatusShouldBeUnprocessableEntity() throws Exception {

        var createDto = CreateProductDto.builder()
                .name("Laptop")
                .supplierId(10000L)
                .categoryId(savedCategory.getId())
                .unitId(savedUnit.getId())
                .purchasePrice(10.99)
                .salePrice(20.99)
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createProductWhenCategoryNotExistsStatusShouldBeUnprocessableEntity() throws Exception {
        var createDto = CreateProductDto.builder()
                .name("Laptop")
                .supplierId(savedSupplier.getId())
                .categoryId(10000L)
                .unitId(savedUnit.getId())
                .purchasePrice(10.99)
                .salePrice(20.99)
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createProductWhenUnitNotExistsStatusShouldBeUnprocessableEntity() throws Exception {
        var createDto = CreateProductDto.builder()
                .name("Laptop")
                .supplierId(savedSupplier.getId())
                .categoryId(savedCategory.getId())
                .unitId(10000L)
                .purchasePrice(10.99)
                .salePrice(20.99)
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createProductWhenPurchasePriceGreaterThanSalePriceStatusShouldBeUnprocessableEntity() throws Exception {

        var createDto = CreateProductDto.builder()
                .name("Laptop")
                .supplierId(savedSupplier.getId())
                .categoryId(savedCategory.getId())
                .unitId(savedUnit.getId())
                .purchasePrice(100.99)
                .salePrice(20.99)
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }
}
