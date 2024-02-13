package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.orders.*;
import com.jcanseco.inventoryapi.entities.Stock;
import com.jcanseco.inventoryapi.repositories.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StockRepository stockRepository;

    @WithMockUser(authorities = {"Permissions.Orders.Create"})
    @Sql("/multiple-orders.sql")
    @Test
    public void createOrderWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var customerId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 10L);
            put(2L, 10L);
        }};

        var dto = CreateOrderDto.builder()
                .customerId(customerId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        mockMvc.perform(
                        post("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }



    @WithMockUser(authorities = {"Permissions.Orders.Update"})
    @Sql("/multiple-orders.sql")
    @Test
    public void updateOrderWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var orderId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 5L);
            put(3L, 10L);
            put(4L, 7L);
        }};
        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        mockMvc.perform(
                        put("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @WithMockUser(authorities = {"Permissions.Orders.Delete"})
    @Sql("/multiple-orders.sql")
    @Test
    public void deleteOrderStatusShouldBeNoContent() throws Exception {
        var orderId = 4L;
        mockMvc.perform(
                        delete("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @WithMockUser(authorities = "Permissions.Orders.Deliver")
    @Sql("/multiple-orders.sql")
    @Test
    public void deliverOrderStatusShouldBeNoContent() throws Exception {
        var orderId = 1L;
        mockMvc.perform(
                        put("/api/orders/" + orderId + "/deliver")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
        var stocks = stockRepository.findAllById(List.of(1L, 2L));
        for (Stock stock : stocks) {
            assertEquals(0L, stock.getQuantity());
        }
    }

    @WithMockUser(authorities = "Permissions.Orders.View")
    @Sql("/multiple-orders.sql")
    @Test
    public void getOrderByIdStatusShouldBeOk() throws Exception {

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
                        .price(150d)
                        .total(1500d)
                        .build(),
                OrderItemDto.builder()
                        .id(20L)
                        .productId(20L)
                        .productName("Sleeping Bag")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(60d)
                        .total(600d)
                        .build()
        );

        var expectedOrder = OrderDetailsDto.builder()
                .id(orderId)
                .customer(customer)
                .items(items)
                .total(2100d)
                .orderedAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .deliveredAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .delivered(true)
                .build();

        var result = mockMvc.perform(
                        get("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        OrderDetailsDto orderResult = mapper.readValue(content, OrderDetailsDto.class);
        assertEquals(expectedOrder, orderResult);
    }


    @WithMockUser(authorities = "Permissions.Orders.View")
    @Sql("/multiple-orders.sql")
    @Test
    public void getOrdersStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "total")
                                .param("sortOrder", "asc")
                                .param("delivered", "true")
                                .param("orderedAtStartDate","2023-06-05T00:00:00")
                                .param("orderedAtEndDate", "2023-07-03T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }


    @WithMockUser(authorities = "Permissions.Orders.View")
    @Sql("/multiple-orders.sql")
    @Test
    public void getOrdersPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "1")
                                .param("pageSize", "2")
                                .param("orderBy", "total")
                                .param("sortOrder", "asc")
                                .param("delivered", "true")
                                .param("deliveredAtStartDate","2023-06-05T00:00:00")
                                .param("deliveredAtEndDate", "2023-07-03T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
