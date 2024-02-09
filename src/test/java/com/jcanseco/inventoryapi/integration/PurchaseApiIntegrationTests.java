package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.purchases.*;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.repositories.PurchaseRepository;
import com.jcanseco.inventoryapi.repositories.StockRepository;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class PurchaseApiIntegrationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StockRepository stockRepository;

    private static HttpHeaders httpHeaders;

    @BeforeAll
    public static void setup() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Sql("/multiple-purchases.sql")
    @Test
    public void createPurchaseWhenModelIsValidStatusShouldBeCreated() throws JsonProcessingException {
        var supplierId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 10L);
            put(2L, 10L);
        }};

        var dto = CreatePurchaseDto.builder()
                .supplierId(supplierId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }


    @Sql("/multiple-purchases.sql")
    @Test
    public void updatePurchaseWhenModelIsValidStatusShouldBeNoContent() throws JsonProcessingException {
        var purchaseId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 5L);
            put(3L, 10L);
            put(4L, 7L);
        }};
        var url = baseUrl() + "/" + purchaseId;
        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-purchases.sql")
    @Test
    public void deletePurchaseStatusShouldBeNoContent() {
        var purchaseId = 4L;
        var url = baseUrl() + "/" + purchaseId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-purchases.sql")
    @Test
    public void receivePurchaseStatusShouldBeNoContent() {
        var purchaseId = 1L;
        var url = baseUrl() + "/" + purchaseId + "/receive";
        var response = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());

        var stocks = stockRepository.findAllById(List.of(1L, 2L));
        stocks.forEach(s -> assertEquals(20L, s.getQuantity()));
    }

    @Sql("/multiple-purchases.sql")
    @Test
    public void getPurchaseByIdStatusShouldBeOk() {

        var purchaseId = 10L;

        var supplier = SupplierDto.builder()
                .id(2L)
                .companyName("Tech Solutions Inc")
                .contactName("Alice Brown")
                .contactPhone("555-1234-4")
                .build();

        var items = List.of(
                PurchaseItemDto.builder()
                        .id(19L)
                        .productId(19L)
                        .productName("Vacuum Cleaner")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("90.00"))
                        .total(new BigDecimal("900.00"))
                        .build(),
                PurchaseItemDto.builder()
                        .id(20L)
                        .productId(20L)
                        .productName("Sleeping Bag")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("40.00"))
                        .total(new BigDecimal("400.00"))
                        .build()
        );

        var expectedPurchase = PurchaseDetailsDto.builder()
                .id(purchaseId)
                .supplier(supplier)
                .items(items)
                .total(new BigDecimal("1300.00"))
                .orderedAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .arrivedAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .arrived(true)
                .build();

        var url = baseUrl() + "/" + purchaseId;
        var response = restTemplate.exchange(url, HttpMethod.GET, null, PurchaseDetailsDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        var result = response.getBody();
        assertNotNull(result);
        assertEquals(expectedPurchase, result);
    }


    // TODO:
    //  The current test cases are functional, but I believe there's room for improvement in terms of readability.
    //  I plan to enhance the clarity of the tests, address multiple edge cases for robustness, and consider refactoring the URL construction process.
    //  One potential approach is to implement a builder pattern to streamline URL building.
    //  Given the time constraints, my plan is to gradually implement these improvements.

    @Sql("/multiple-purchases.sql")
    @Test
    public void getPurchasesStatusShouldBeOk() {
        var url = baseUrl() + "?orderBy=total&sortOrder=asc&arrived=true&orderedAtStartDate=2023-06-05T00:00:00&orderedAtEndDate=2023-07-03T00:00:00";
        var responseType = new ParameterizedTypeReference<List<PurchaseDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var purchases = response.getBody();
        assertEquals(5, purchases.size());
    }

    @Sql("/multiple-purchases.sql")
    @Test
    public void getPurchasesPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=2&orderBy=total&sortOrder=asc&arrived=true&arrivedAtStartDate=2023-06-05T00:00:00&arrivedAtEndDate=2023-07-03T00:00:00";
        var responseType = new ParameterizedTypeReference<PagedList<PurchaseDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var pagedList = response.getBody();
        assertEquals(1, pagedList.getPageNumber());
        assertEquals(2, pagedList.getPageSize());
        assertEquals(3, pagedList.getTotalPages());
        assertEquals(5, pagedList.getTotalElements());
    }


    private String baseUrl() {
        return String.format("http://localhost:%d/api/purchases", port);
    }
}
