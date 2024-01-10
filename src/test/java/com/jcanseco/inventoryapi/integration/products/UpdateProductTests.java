package com.jcanseco.inventoryapi.integration.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.products.UpdateProductDto;
import com.jcanseco.inventoryapi.entities.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateProductTests {

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
    private Supplier updateSupplier;
    private Category updateCategory;
    private UnitOfMeasurement updateUnit;
    private Product savedProduct;

    private void setupSavedEntities() {

        var address = Address
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var supplier = supplierRepository.saveAndFlush(Supplier.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build()
        );

        var category = categoryRepository.saveAndFlush(Category.builder()
                .name("Electronics")
                .build()
        );

        var unit = unitRepository.saveAndFlush(UnitOfMeasurement.builder()
                .name("Piece")
                .build()
        );


        var product = Product.builder()
                .name("Laptop")
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .quantity(0L)
                .purchasePrice(BigDecimal.valueOf(10.99))
                .salePrice(BigDecimal.valueOf(20.99))
                .build();

        savedProduct = productRepository.saveAndFlush(product);
    }

    private void setupEntitiesForUpdate() {
        var address = Address
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var supplier = Supplier.builder()
                .companyName("DCE Corp")
                .contactName("Jane Doe")
                .contactPhone("555-1234-2")
                .address(address)
                .build();

        var category = Category.builder()
                .name("Games")
                .build();

        var unit = UnitOfMeasurement.builder()
                .name("Each")
                .build();

        updateSupplier = supplierRepository.saveAndFlush(supplier);
        updateCategory = categoryRepository.saveAndFlush(category);
        updateUnit = unitRepository.saveAndFlush(unit);
    }

    @BeforeEach
    public void setup() {
        setupSavedEntities();
        setupEntitiesForUpdate();
    }

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    public void updateProductWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var productId = savedProduct.getId();
        var supplierId = updateSupplier.getId();
        var categoryId = updateCategory.getId();
        var unitId = updateUnit.getId();

        var updateDto = UpdateProductDto.builder()
                .productId(productId)
                .name("Xbox")
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .purchasePrice(199.99)
                .salePrice(299.99)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateProductWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var productId = savedProduct.getId();
        var supplierId = updateSupplier.getId();
        var categoryId = updateCategory.getId();
        var unitId = updateUnit.getId();

        var updateDto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .purchasePrice(199.99)
                .salePrice(299.99)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProductWhenProductDoesNotExistsStatusShouldBeNotFound() throws Exception {

        var productId = 10000L;
        var supplierId = updateSupplier.getId();
        var categoryId = updateCategory.getId();
        var unitId = updateUnit.getId();

        var updateDto = UpdateProductDto.builder()
                .name("Xbox")
                .productId(productId)
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .purchasePrice(199.99)
                .salePrice(299.99)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProductWhenSupplierDoesNotExistsStatusShouldBeUnprocessableEntity() throws Exception {

        var productId = savedProduct.getId();
        var supplierId = 10000L;
        var categoryId = updateCategory.getId();
        var unitId = updateUnit.getId();

        var updateDto = UpdateProductDto.builder()
                .productId(productId)
                .name("Xbox")
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .purchasePrice(199.99)
                .salePrice(299.99)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    public void updateProductWhenCategoryDoesNotExistsStatusShouldBeUnprocessableEntity() throws Exception {

        var productId = savedProduct.getId();
        var supplierId = updateSupplier.getId();
        var categoryId = 10000L;
        var unitId = updateUnit.getId();

        var updateDto = UpdateProductDto.builder()
                .productId(productId)
                .name("Xbox")
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .purchasePrice(199.99)
                .salePrice(299.99)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    public void updateProductWhenUnitDoesNotExistsStatusShouldBeUnprocessableEntity() throws Exception {
        var productId = savedProduct.getId();
        var supplierId = updateSupplier.getId();
        var categoryId = updateCategory.getId();
        var unitId = 10000L;

        var updateDto = UpdateProductDto.builder()
                .productId(productId)
                .name("Xbox")
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .purchasePrice(199.99)
                .salePrice(299.99)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateProductWhenPurchasePriceIsGreaterThanSalePriceStatusShouldBeUnprocessableEntity() throws Exception {

        var productId = savedProduct.getId();
        var supplierId = updateSupplier.getId();
        var categoryId = updateCategory.getId();
        var unitId = updateUnit.getId();

        var updateDto = UpdateProductDto.builder()
                .productId(productId)
                .name("Xbox")
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .purchasePrice(599.99)
                .salePrice(299.99)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }
}
