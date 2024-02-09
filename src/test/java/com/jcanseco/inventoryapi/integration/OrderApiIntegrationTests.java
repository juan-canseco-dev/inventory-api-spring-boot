package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.controllers.SupplierController;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.orders.*;
import com.jcanseco.inventoryapi.entities.Stock;
import com.jcanseco.inventoryapi.repositories.StockRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest(
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class OrderApiIntegrationTests {

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

    @Sql("/multiple-orders.sql")
    @Test
    public void createOrderWhenModelIsValidStatusShouldBeCreated() throws JsonProcessingException {

        var customerId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 10L);
            put(2L, 10L);
        }};

        var dto = CreateOrderDto.builder()
                .customerId(customerId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }


    @Sql("/multiple-orders.sql")
    @Test
    public void updateOrderWhenModelIsValidStatusShouldBeNoContent() throws JsonProcessingException {
        var orderId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 5L);
            put(3L, 10L);
            put(4L, 7L);
        }};
        var url = baseUrl() + "/" + orderId;
        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-orders.sql")
    @Test
    public void deleteOrderStatusShouldBeNoContent() {
        var orderId = 4L;
        var url = baseUrl() + "/" + orderId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-orders.sql")
    @Test
    public void deliverOrderStatusShouldBeNoContent() {
        var orderId = 1L;
        var url = baseUrl() + "/" + orderId + "/deliver";
        var response = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
        var stocks = stockRepository.findAllById(List.of(1L, 2L));
        for (Stock stock : stocks) {
            assertEquals(0L, stock.getQuantity());
        }
    }

    @Sql("/multiple-orders.sql")
    @Test
    public void getOrderByIdStatusShouldBeOk() {

        var orderId = 10L;

        var customer = CustomerDto.builder()
                .id(2L)
                .dni("987654321")
                .fullName("Jane Smith")
                .phone("555-1234-2")
                .build();

        var items = List.of(
                OrderItemDto.builder()
                        .id(19L)
                        .productId(19L)
                        .productName("Vacuum Cleaner")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("150.00"))
                        .total(new BigDecimal("1500.00"))
                        .build(),
                OrderItemDto.builder()
                        .id(20L)
                        .productId(20L)
                        .productName("Sleeping Bag")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("60.00"))
                        .total(new BigDecimal("600.00"))
                        .build()
        );

        var expectedOrder = OrderDetailsDto.builder()
                .id(orderId)
                .customer(customer)
                .items(items)
                .total(new BigDecimal("2100.00"))
                .orderedAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .deliveredAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .delivered(true)
                .build();

        var url = baseUrl() + "/" + orderId;
        var response = restTemplate.exchange(url, HttpMethod.GET, null, OrderDetailsDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        var result = response.getBody();
        assertNotNull(result);
        assertEquals(expectedOrder, result);
    }


    // TODO:
    //  The current test cases are functional, but I believe there's room for improvement in terms of readability.
    //  I plan to enhance the clarity of the tests, address multiple edge cases for robustness, and consider refactoring the URL construction process.
    //  One potential approach is to implement a builder pattern to streamline URL building.
    //  Given the time constraints, my plan is to gradually implement these improvements.

    @Sql("/multiple-orders.sql")
    @Test
    public void getOrdersStatusShouldBeOk() {
        var url = baseUrl() + "?orderBy=total&sortOrder=asc&delivered=true&orderedAtStartDate=2023-06-05T00:00:00&orderedAtEndDate=2023-07-03T00:00:00";
        var responseType = new ParameterizedTypeReference<List<OrderDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var orders = response.getBody();
        assertEquals(5, orders.size());
    }

    @Sql("/multiple-orders.sql")
    @Test
    public void getOrdersPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=2&orderBy=total&sortOrder=asc&delivered=true&deliveredAtStartDate=2023-06-05T00:00:00&deliveredAtEndDate=2023-07-03T00:00:00";
        var responseType = new ParameterizedTypeReference<PagedList<OrderDto>>() {};
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
        return String.format("http://localhost:%d/api/orders", port);
    }
}
