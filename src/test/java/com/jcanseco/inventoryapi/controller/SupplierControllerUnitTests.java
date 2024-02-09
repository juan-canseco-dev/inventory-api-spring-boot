package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.SupplierController;
import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.*;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.services.JwtService;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.services.RoleService;
import com.jcanseco.inventoryapi.security.services.UserService;
import com.jcanseco.inventoryapi.services.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@MockBeans({
        @MockBean(CustomerService.class),
        @MockBean(ProductService.class),
        @MockBean(CategoryService.class),
        @MockBean(UnitService.class),
        @MockBean(PurchaseService.class),
        @MockBean(OrderService.class),
        @MockBean(ResourceService.class),
        @MockBean(RoleService.class),
        @MockBean(UserService.class),
        @MockBean(JwtService.class)
})
@WebMvcTest(
        controllers = SupplierController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class SupplierControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private SupplierService supplierService;

    private AddressDto defaultAddress() {
        return AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    @Test
    public void createSupplierWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-2")
                .address(defaultAddress())
                .build();

        when(supplierService.createSupplier(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/suppliers")
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
    public void createSupplierWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var dto = CreateSupplierDto.builder()
                .companyName("")
                .contactName("John Doe")
                .contactPhone("555-1234-2")
                .address(defaultAddress())
                .build();

        when(supplierService.createSupplier(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateSupplierWhenModelIsValidStatusShouldBeNoContent() throws Exception {
        
        var supplierId = 1L;

        var updateSupplierDto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-2")
                .address(defaultAddress())
                .build();
        
        doNothing().when(supplierService).updateSupplier(updateSupplierDto);

        mockMvc.perform(
                        put("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateSupplierDto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateSupplierWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var supplierId = 1L;

        var updateSupplierDto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp")
                .contactName("")
                .contactPhone("555-1234-2")
                .address(defaultAddress())
                .build();

        doNothing().when(supplierService).updateSupplier(updateSupplierDto);

        mockMvc.perform(
                        put("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateSupplierDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateSupplierWhenModelAndPathIdAreDifferentStatusShouldBeBadRequest() throws Exception {
        var supplierId = 1L;

        var updateSupplierDto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-2")
                .address(defaultAddress())
                .build();

        doNothing().when(supplierService).updateSupplier(updateSupplierDto);

        mockMvc.perform(
                        put("/api/suppliers/5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateSupplierDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateSupplierWhenSupplierDoNotExistsStatusShouldBeNotFound() throws Exception {

        var supplierId = 1L;

        var updateSupplierDto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-2")
                .address(defaultAddress())
                .build();

        doThrow(new NotFoundException(String.format("Supplier with the Id {%d} was not found.", supplierId)))
                .when(supplierService)
                .updateSupplier(updateSupplierDto);

        mockMvc.perform(
                        put("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateSupplierDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteSupplierWhenSupplierExistsStatusShouldBeNoContent() throws Exception {
        var supplierId = 1L;
        doNothing().when(supplierService).deleteSupplier(supplierId);
        mockMvc.perform(
                        delete("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteSupplierWhenSupplierDoNotExistsStatusShouldBeNotFound() throws Exception {
        var supplierId = 1L;
        doThrow(new NotFoundException(String.format("Supplier with the Id {%d} was not found.", supplierId)))
                .when(supplierService)
                .deleteSupplier(supplierId);
        mockMvc.perform(
                        delete("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSupplierByIdWhenSupplierExistsStatusShouldBeOk() throws Exception {
        var supplierId = 1L;
        var supplierDto = SupplierDetailsDto.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-2")
                .address(defaultAddress())
                .build();
        when(supplierService.getSupplierById(supplierId)).thenReturn(supplierDto);
        mockMvc.perform(
                        get("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getSupplierByIdWhenSupplierDoNotExistsStatusShouldBeNotFound() throws Exception {
        var supplierId = 1L;
        doThrow(new NotFoundException(String.format("Supplier with the Id {%d} was not found.", supplierId)))
                .when(supplierService).getSupplierById(supplierId);
        mockMvc.perform(
                        get("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSuppliersWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getSuppliersWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getSuppliersPageWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getSuppliersWhenPageSizeAndPageNumberAreNotPresentStatusShouldBeOk() throws Exception {


        var getSuppliersRequest = GetSuppliersRequest.builder()
                .build();

        var suppliers = List.of(
                SupplierDto.builder()
                        .id(1L)
                        .companyName("ABC Corp")
                        .contactName("John Doe")
                        .contactPhone("555-1234-1")
                        .build(),
                SupplierDto.builder()
                        .id(2L)
                        .companyName("DFG Corp")
                        .contactName("Jane Doe")
                        .contactPhone("555-1234-2")
                        .build()
        );

        when(supplierService.getSuppliers(getSuppliersRequest)).thenReturn(suppliers);

        mockMvc.perform(
                        get("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getSuppliersPageWhenPageSizeAndPageNumberArePresentStatusShouldBeOk() throws Exception {

        var getSuppliersPageRequest = GetSuppliersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        var suppliers = List.of(
                SupplierDto.builder()
                        .id(1L)
                        .companyName("ABC Corp")
                        .contactName("John Doe")
                        .contactPhone("555-1234-1")
                        .build()
        );

        var pagedList = new PagedList<>(suppliers, 1, 1, 2, 2);

        when(supplierService.getSuppliersPaged(getSuppliersPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getSuppliersPageRequest.getPageNumber().toString())
                                .param("pageSize", getSuppliersPageRequest.getPageSize().toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
