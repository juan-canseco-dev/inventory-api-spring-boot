package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.purchases.*;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
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
public class PurchaseControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StockRepository stockRepository;

    @WithMockUser(authorities = {"Permissions.Purchases.Create"})
    @Sql("/multiple-purchases.sql")
    @Test
    public void createPurchaseWhenModelIsValidStatusShouldBeCreated() throws Exception {
        var supplierId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 10L);
            put(2L, 10L);
        }};

        var dto = CreatePurchaseDto.builder()
                .supplierId(supplierId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        mockMvc.perform(
                        post("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }



    @WithMockUser(authorities = {"Permissions.Purchases.Update"})
    @Sql("/multiple-purchases.sql")
    @Test
    public void updatePurchaseWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var purchaseId = 1L;
        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(1L, 5L);
            put(3L, 10L);
            put(4L, 7L);
        }};

        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @WithMockUser(authorities = {"Permissions.Purchases.Delete"})
    @Sql("/multiple-purchases.sql")
    @Test
    public void deletePurchaseStatusShouldBeNoContent() throws Exception {
        var purchaseId = 4L;
        mockMvc.perform(
                        delete("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @WithMockUser(authorities = {"Permissions.Purchases.Receive"})
    @Sql("/multiple-purchases.sql")
    @Test
    public void receivePurchaseStatusShouldBeNoContent() throws Exception {
        var purchaseId = 1L;
        mockMvc.perform(
                        put("/api/purchases/" + purchaseId + "/receive")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());

        var stocks = stockRepository.findAllById(List.of(1L, 2L));
        stocks.forEach(s -> assertEquals(20L, s.getQuantity()));
    }

    @WithMockUser(authorities = {"Permissions.Purchases.View"})
    @Sql("/multiple-purchases.sql")
    @Test
    public void getPurchaseByIdStatusShouldBeOk() throws Exception {

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
                        .price(90.00)
                        .total(900.00)
                        .build(),
                PurchaseItemDto.builder()
                        .id(20L)
                        .productId(20L)
                        .productName("Sleeping Bag")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(40.00)
                        .total(400.00)
                        .build()
        );

        var expectedPurchase = PurchaseDetailsDto.builder()
                .id(purchaseId)
                .supplier(supplier)
                .items(items)
                .total(1300.00)
                .orderedAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .arrivedAt(LocalDateTime.of(2023, Month.JULY, 3,0,0))
                .arrived(true)
                .build();

        var result = mockMvc.perform(
                        get("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        PurchaseDetailsDto purchaseResult = mapper.readValue(content, PurchaseDetailsDto.class);
        assertEquals(expectedPurchase, purchaseResult);
    }


    @WithMockUser(authorities = {"Permissions.Purchases.View"})
    @Sql("/multiple-purchases.sql")
    @Test
    public void getPurchasesStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "total")
                                .param("sortOrder", "asc")
                                .param("arrived", "true")
                                .param("orderedAtStartDate","2023-06-05T00:00:00")
                                .param("orderedAtEndDate", "2023-07-03T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @WithMockUser(authorities = {"Permissions.Purchases.View"})
    @Sql("/multiple-purchases.sql")
    @Test
    public void getPurchasesPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "1")
                                .param("pageSize", "2")
                                .param("orderBy", "total")
                                .param("sortOrder", "asc")
                                .param("arrived", "true")
                                .param("arrivedAtStartDate","2023-06-05T00:00:00")
                                .param("arrivedAtEndDate", "2023-07-03T00:00:00")
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
