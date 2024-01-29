package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.OrderController;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.orders.CreateOrderDto;
import com.jcanseco.inventoryapi.dtos.orders.GetOrdersRequest;
import com.jcanseco.inventoryapi.dtos.orders.OrderDto;
import com.jcanseco.inventoryapi.dtos.orders.UpdateOrderDto;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
        @MockBean(CustomerService.class),
        @MockBean(CategoryService.class),
        @MockBean(SupplierService.class),
        @MockBean(UnitService.class),
        @MockBean(ProductService.class),
        @MockBean(PurchaseService.class),
        @MockBean(ResourceService.class)
})
@WebMvcTest(
        controllers = OrderController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class OrderControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private OrderService service;
    @MockBean
    private ProductRepository productRepository;
    private Long customerId;
    private Long orderId;
    private HashMap<Long, Long> validProductsWithQuantities;
    private HashMap<Long, Long> invalidProductsWithQuantities;

    @BeforeEach
    public void setup() {
        customerId = 1L;
        orderId = 1L;
        validProductsWithQuantities = new HashMap<>(){{
            put(1L, 10L);
            put(2L, 20L);
        }};
        invalidProductsWithQuantities = new HashMap<>() {{
            put(1L, 0L);
            put(2L, -1L);
        }};
    }

    @Test
    public void createOrderWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateOrderDto.builder()
                .customerId(customerId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        when(service.createOrder(dto)).thenReturn(customerId);

        mockMvc.perform(
                        post("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber())
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    public void createOrderWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception  {

        var dto = CreateOrderDto.builder()
                .customerId(customerId)
                .productsWithQuantities(invalidProductsWithQuantities)
                .build();

        when(service.createOrder(dto)).thenReturn(customerId);

        mockMvc.perform(
                        post("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateOrderWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        doNothing().when(service).updateOrder(dto);

        mockMvc.perform(
                        put("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateOrderWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(invalidProductsWithQuantities)
                .build();

        doNothing().when(service).updateOrder(dto);

        mockMvc.perform(
                        put("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateOrderWhenModelIdAndPathIdNotEqualsStatusShouldBeBadRequest() throws Exception {
        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        doNothing().when(service).updateOrder(dto);

        mockMvc.perform(
                        put("/api/orders/10")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateOrderWhenOrderIsDeliveredStatusShouldBeUnprocessableEntity() throws Exception {

        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        doThrow(new DomainException("A delivered Order cannot be updated"))
                .when(service)
                .updateOrder(dto);

        mockMvc.perform(
                        put("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateOrderWhenOrderDoNotExistsStatusShouldBeNotFound() throws Exception {
        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        doThrow(new NotFoundException("The Order was not found"))
                .when(service)
                .updateOrder(dto);

        mockMvc.perform(
                        put("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deliverOrderWhenOrdersIsNotDeliveredStatusShouldBeNoContent() throws Exception {

        doNothing().when(service).deliverOrder(orderId);

        mockMvc.perform(
                        put("/api/orders/" + orderId + "/deliver")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deliverOrderWhenOrderIsDeliveredStatusShouldBeUnprocessableEntity() throws Exception {
        doThrow(new DomainException("The Order is already delivered."))
                .when(service)
                .deliverOrder(orderId);

        mockMvc.perform(
                        put("/api/orders/" + orderId + "/deliver")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deliverOrderWhenOrderDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("The Order is already delivered"))
                .when(service)
                .deliverOrder(orderId);

        mockMvc.perform(
                        put("/api/orders/" + orderId + "/deliver")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOrderWhenOrderIsNotDeliveredStatusShouldBeNoContent() throws Exception {

        doNothing().when(service).deleteOrder(orderId);

        mockMvc.perform(
                        delete("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteOrderWhenOrderIsDeliveredStatusShouldBeUnprocessableEntity() throws Exception {

        doThrow(new DomainException("Cannot delete a order that already has been delivered."))
                .when(service)
                .deleteOrder(orderId);

        mockMvc.perform(
                        delete("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteOrderWhenOrderDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("Order Not Found."))
                .when(service)
                .deleteOrder(orderId);

        mockMvc.perform(
                        delete("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOrdersByIdWhenOrderExistsStatusShouldBeOk() throws Exception {
        when(service.getOrderById(orderId)).thenReturn(mock());
        mockMvc.perform(
                        get("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getOrderByIdWhenOrderDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("Order Not Found"))
                .when(service)
                .getOrderById(orderId);
        mockMvc.perform(
                        get("/api/orders/" + orderId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOrdersWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOrdersWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOrdersWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOrdersWhenPageNumberAndPageSizeAreNotPresentStatusShouldBeOk() throws Exception {
        var getOrdersRequest = GetOrdersRequest.builder()
                .build();

        List<OrderDto> orders = List.of(
                mock(),
                mock()
        );

        when(service.getOrders(getOrdersRequest)).thenReturn(orders);

        mockMvc.perform(
                        get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getOrdersWhenPageNumberAndPageSizeArePresentStatusShouldBeOk() throws Exception {

        var getOrdersRequest = GetOrdersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        List<OrderDto> orders = List.of(
                OrderDto.builder().build()
        );

        var pagedList = new PagedList<>(orders, 1, 1, 2, 2);

        when(service.getOrdersPage(getOrdersRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getOrdersRequest.getPageNumber().toString())
                                .param("pageSize", getOrdersRequest.getPageSize().toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.items", notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
