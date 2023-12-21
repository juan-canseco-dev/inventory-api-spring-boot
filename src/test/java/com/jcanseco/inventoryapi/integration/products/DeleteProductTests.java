package com.jcanseco.inventoryapi.integration.products;

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
public class DeleteProductTests {

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
    private Product savedProduct;

    @BeforeEach
    public void setup() {

        var address = SupplierAddress
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

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    public void deleteProductWhenProductExistsStatusShouldBeOk() throws Exception {
        var productId = savedProduct.getId();

        var request = MockMvcRequestBuilders
                .delete("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProductWhenProductDoesNoExistsStatusShouldBeNotFound() throws Exception {

        var productId = 7000L;

        var request = MockMvcRequestBuilders
                .delete("/api/products/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
