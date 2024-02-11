package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.products.CreateProductDto;
import com.jcanseco.inventoryapi.dtos.products.ProductDetailsDto;
import com.jcanseco.inventoryapi.dtos.products.ProductDto;
import com.jcanseco.inventoryapi.dtos.products.UpdateProductDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class ProductApiIntegrationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    private static HttpHeaders httpHeaders;

    @BeforeAll
    public static void setup() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Sql("/multiple-products.sql")
    @Test
    public void createProductStatusShouldBeCreated() throws JsonProcessingException {

        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("New Product")
                .purchasePrice(49.99)
                .salePrice(59.99)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    @Sql("/multiple-products.sql")
    @Test
    public void updateProductStatusShouldBeNoContent() throws JsonProcessingException {
        var productId = 1L;
        var url = baseUrl() + "/" + productId;
        var dto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(2L)
                .categoryId(2L)
                .unitId(2L)
                .name("Asus RGX Laptop")
                .purchasePrice(999.99)
                .salePrice(1399.99)
                .build();
        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-products.sql")
    @Test
    public void deleteProductStatusShouldBeNoContent() {
        var productId = 20L;
        var url = baseUrl() + "/" + productId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-products.sql")
    @Test
    public void getProductByIdStatusShouldBeOk() {
        var productId = 1L;
        var url = baseUrl() + "/" + productId;
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

        var response = restTemplate.exchange(url, HttpMethod.GET, null, ProductDetailsDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var product = response.getBody();
        assertEquals(expectedProduct, product);
    }

    @Sql("/multiple-products.sql")
    @Test
    public void getProductsStatusShouldBeOk() {
        var url = baseUrl() + "?orderBy=name&sortOrder=asc&name=s&supplierId=2&categoryId=2&unitId=2";
        var responseType = new ParameterizedTypeReference<List<ProductDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var products = response.getBody();
        assertEquals(6, products.size());
    }

    @Sql("/multiple-products.sql")
    @Test
    public void getProductsPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=2&orderBy=name&sortOrder=asc&name=s&supplierId=2&categoryId=2&unitId=2";
        var responseType = new ParameterizedTypeReference<PagedList<ProductDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var pagedList = response.getBody();
        assertEquals(1, pagedList.getPageNumber());
        assertEquals(2, pagedList.getPageSize());
        assertEquals(3, pagedList.getTotalPages());
        assertEquals(6, pagedList.getTotalElements());
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/products", port);
    }
}
