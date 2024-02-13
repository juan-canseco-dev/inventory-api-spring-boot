package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.products.CreateProductDto;
import com.jcanseco.inventoryapi.dtos.products.ProductDetailsDto;;
import com.jcanseco.inventoryapi.dtos.products.UpdateProductDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @WithMockUser(authorities = {"Permissions.Products.Create"})
    @Sql("/multiple-products.sql")
    @Test
    public void createProductStatusShouldBeCreated() throws Exception {

        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("New Product")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();

        mockMvc.perform(
                        post("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }


    @WithMockUser(authorities = {"Permissions.Products.Update"})
    @Sql("/multiple-products.sql")
    @Test
    public void updateProductStatusShouldBeNoContent() throws Exception {
        var productId = 1L;
        var dto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(2L)
                .categoryId(2L)
                .unitId(2L)
                .name("Asus RGX Laptop")
                .purchasePrice(999.99)
                .salePrice(1399.99)
                .build();
        mockMvc.perform(
                        put("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @WithMockUser(authorities = {"Permissions.Products.Delete"})
    @Sql("/multiple-products.sql")
    @Test
    public void deleteProductStatusShouldBeNoContent() throws Exception {
        mockMvc.perform(
                        delete("/api/products/20")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }



    @WithMockUser(authorities = {"Permissions.Products.View"})
    @Sql("/multiple-products.sql")
    @Test
    public void getProductByIdStatusShouldBeOk() throws Exception {
        var productId = 1L;
        var expectedProduct = ProductDetailsDto.builder()
                .id(productId)
                .supplier(
                        SupplierDto.builder()
                                .id(1L)
                                .companyName("ABC Corp")
                                .contactName("John Doe")
                                .contactPhone("555-1234-1")
                                .build()
                )
                .category(
                        CategoryDto.builder()
                                .id(1L)
                                .name("Electronics")
                                .build()
                )
                .unit(
                        UnitOfMeasurementDto.builder()
                                .id(1L)
                                .name("Piece")
                                .build()
                )
                .name("Laptop")
                .stock(10L)
                .purchasePrice(800.00)
                .salePrice(1200.00)
                .build();

        var result = mockMvc.perform(
                        get("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ProductDetailsDto productResult = mapper.readValue(content, ProductDetailsDto.class);
        assertEquals(expectedProduct, productResult);
    }


    @WithMockUser(authorities = {"Permissions.Products.View"})
    @Sql("/multiple-products.sql")
    @Test
    public void getProductsStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "name")
                                .param("sortOrder", "asc")
                                .param("name", "s")
                                .param("supplierId","2")
                                .param("categoryId", "2")
                                .param("unitId", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(6)));
    }


    @WithMockUser(authorities = {"Permissions.Products.View"})
    @Sql("/multiple-products.sql")
    @Test
    public void getProductsPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "1")
                                .param("pageSize", "2")
                                .param("orderBy", "name")
                                .param("sortOrder", "asc")
                                .param("name", "s")
                                .param("supplierId","2")
                                .param("categoryId", "2")
                                .param("unitId", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(6))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
