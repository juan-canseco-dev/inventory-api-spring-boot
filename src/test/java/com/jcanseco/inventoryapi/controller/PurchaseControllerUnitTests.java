package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.PurchaseController;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.purchases.CreatePurchaseDto;
import com.jcanseco.inventoryapi.dtos.purchases.GetPurchasesRequest;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseDto;
import com.jcanseco.inventoryapi.dtos.purchases.UpdatePurchaseDto;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.services.RoleService;
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
        @MockBean(OrderService.class),
        @MockBean(ResourceService.class),
        @MockBean(RoleService.class)
})
@WebMvcTest(
        controllers = PurchaseController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class PurchaseControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PurchaseService service;
    @MockBean
    private ProductRepository productRepository;
    private Long supplierId;
    private Long purchaseId;
    private HashMap<Long, Long> validProductsWithQuantities;
    private HashMap<Long, Long> invalidProductsWithQuantities;

    @BeforeEach
    public void setup() {
        supplierId = 1L;
        purchaseId = 1L;
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
    public void createPurchaseWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreatePurchaseDto.builder()
                .supplierId(supplierId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        when(service.createPurchase(dto)).thenReturn(supplierId);

        mockMvc.perform(
                        post("/api/purchases")
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
    public void createPurchaseWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception  {

        var dto = CreatePurchaseDto.builder()
                .supplierId(supplierId)
                .productsWithQuantities(invalidProductsWithQuantities)
                .build();

        when(service.createPurchase(dto)).thenReturn(supplierId);

        mockMvc.perform(
                        post("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePurchaseWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        doNothing().when(service).updatePurchase(dto);

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updatePurchaseWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(invalidProductsWithQuantities)
                .build();

        doNothing().when(service).updatePurchase(dto);

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePurchaseWhenModelIdAndPathIdNotEqualsStatusShouldBeBadRequest() throws Exception {
        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        doNothing().when(service).updatePurchase(dto);

        mockMvc.perform(
                        put("/api/purchases/10")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePurchaseWhenPurchaseIsArrivedStatusShouldBeUnprocessableEntity() throws Exception {

        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        doThrow(new DomainException("An arrived purchase cannot be updated"))
                .when(service)
                .updatePurchase(dto);

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updatePurchaseWhenPurchaseDoNotExistsStatusShouldBeNotFound() throws Exception {
        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(validProductsWithQuantities)
                .build();

        when(productRepository.existsById(Mockito.any())).thenReturn(true);
        doThrow(new NotFoundException("The Purchase was not found"))
                .when(service)
                .updatePurchase(dto);

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void receivePurchaseWhenPurchaseIsNotArrivedStatusShouldBeNoContent() throws Exception {
        doNothing().when(service).receivePurchase(purchaseId);

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId + "/receive")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void receivePurchaseWhenPurchaseIsArrivedStatusShouldBeUnprocessableEntity() throws Exception {
        doThrow(new DomainException("The Purchase already arrive"))
                .when(service)
                .receivePurchase(purchaseId);

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId + "/receive")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void receivePurchaseWhenPurchaseDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("The Purchase already arrive"))
                .when(service)
                .receivePurchase(purchaseId);

        mockMvc.perform(
                        put("/api/purchases/" + purchaseId + "/receive")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePurchaseWhenPurchaseIsNotArrivedStatusShouldBeNoContent() throws Exception {

        doNothing().when(service).deletePurchase(purchaseId);

        mockMvc.perform(
                        delete("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePurchaseWhenPurchaseIsArrivedStatusShouldBeUnprocessableEntity() throws Exception {

        doThrow(new DomainException("Cannot delete a purchase that already arrive."))
                .when(service)
                .deletePurchase(purchaseId);

        mockMvc.perform(
                        delete("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deletePurchaseWhenPurchaseDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("Purchase Not Found."))
                .when(service)
                .deletePurchase(purchaseId);

        mockMvc.perform(
                        delete("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPurchaseByIdWhenPurchaseExistsStatusShouldBeOk() throws Exception {
        when(service.getPurchaseById(purchaseId)).thenReturn(mock());
        mockMvc.perform(
                        get("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getPurchaseByIdWhenPurchaseDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("Purchase Not Found"))
                .when(service)
                .getPurchaseById(purchaseId);
        mockMvc.perform(
                        get("/api/purchases/" + purchaseId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPurchasesWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPurchasesWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPurchasesWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPurchasesWhenPageNumberAndPageSizeAreNotPresentStatusShouldBeOk() throws Exception {
        var getPurchasesRequest = GetPurchasesRequest.builder()
                .build();

        List<PurchaseDto> purchases = List.of(
                mock(),
                mock()
        );

        when(service.getPurchases(getPurchasesRequest)).thenReturn(purchases);

        mockMvc.perform(
                        get("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getPurchasesWhenPageNumberAndPageSizeArePresentStatusShouldBeOk() throws Exception {

        var getPurchasesPageRequest = GetPurchasesRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        List<PurchaseDto> purchases = List.of(
                PurchaseDto.builder().build()
        );

        var pagedList = new PagedList<>(purchases, 1, 1, 2, 2);

        when(service.getPurchasesPage(getPurchasesPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/purchases")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getPurchasesPageRequest.getPageNumber().toString())
                                .param("pageSize", getPurchasesPageRequest.getPageSize().toString())
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
