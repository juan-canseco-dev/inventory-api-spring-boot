package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.CustomerController;
import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDetailsDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.GetCustomersRequest;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
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
        @MockBean(CategoryService.class),
        @MockBean(ProductService.class),
        @MockBean(SupplierService.class),
        @MockBean(UnitService.class),
        @MockBean(PurchaseService.class),
        @MockBean(OrderService.class),
        @MockBean(ResourceService.class),
        @MockBean(RoleService.class),
        @MockBean(UserService.class),
        @MockBean(JwtService.class)
})
@WebMvcTest(
        controllers = CustomerController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class CustomerControllerUnitTests {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

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
    public void createCustomerWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateCustomerDto.builder()
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress())
                .build();

        when(customerService.createCustomer(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/customers")
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
    public void createCustomerWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var dto = CreateCustomerDto.builder()
                .dni("123456789")
                .fullName("")
                .phone("555-1234-1")
                .address(defaultAddress())
                .build();
        when(customerService.createCustomer(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCustomerWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var customerId = 1L;

        var updateCustomerDto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress())
                .build();

        doNothing().when(customerService).updateCustomer(updateCustomerDto);

        mockMvc.perform(
                        put("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCustomerDto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateCustomerWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var customerId = 1L;

        var updateCustomerDto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName(" ")
                .phone("555-1234-1")
                .address(defaultAddress())
                .build();

        doNothing().when(customerService).updateCustomer(updateCustomerDto);

        mockMvc.perform(
                        put("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCustomerDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCustomerWhenModelAndPathIdAreDifferentStatusShouldBeBadRequest() throws Exception {
        var customerId = 1L;

        var updateCustomerDto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress())
                .build();

        doNothing().when(customerService).updateCustomer(updateCustomerDto);

        mockMvc.perform(
                        put("/api/customers/5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCustomerDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCustomerWhenCustomerDoNotExistsStatusShouldBeNotFound() throws Exception {

        var customerId = 1L;

        var updateCustomerDto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress())
                .build();

        doThrow(new NotFoundException(String.format("Customer with the Id {%d} was not found.", customerId)))
                .when(customerService)
                .updateCustomer(updateCustomerDto);

        mockMvc.perform(
                        put("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCustomerDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCustomerWhenCustomerExistsStatusShouldBeNoContent() throws Exception {
        var customerId = 1L;
        doNothing().when(customerService).deleteCustomer(customerId);
        mockMvc.perform(
                        delete("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCustomerWhenCustomerDoNotExistsStatusShouldBeNotFound() throws Exception {
        var customerId = 1L;
        doThrow(new NotFoundException(String.format("Customer with the Id {%d} was not found.", customerId)))
                .when(customerService)
                .deleteCustomer(customerId);
        mockMvc.perform(
                        delete("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCustomerByIdWhenCustomerExistsStatusShouldBeOk() throws Exception {
        var customerId = 1L;
        var customerDto = CustomerDetailsDto.builder()
                .id(customerId)
                .dni("123456789")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddress())
                .build();
        when(customerService.getCustomerById(customerId)).thenReturn(customerDto);
        mockMvc.perform(
                        get("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getCustomerByIdWhenCustomerDoNotExistsStatusShouldBeNotFound() throws Exception {
        var customerId = 1L;
        doThrow(new NotFoundException(String.format("Customer with the Id {%d} was not found.", customerId)))
                .when(customerService).getCustomerById(customerId);
        mockMvc.perform(
                        get("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCustomersWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCustomersWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCustomersPageWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCustomersWhenPageSizeAndPageNumberAreNotPresentStatusShouldBeOk() throws Exception {


        var getCustomersRequest = GetCustomersRequest.builder()
                .build();

        var customers = List.of(
                CustomerDto.builder()
                        .id(1L)
                        .dni("123456789")
                        .phone("555-1234-1")
                        .fullName("John Doe")
                        .build(),

                CustomerDto.builder()
                        .id(2L)
                        .dni("987654321")
                        .phone("555-1234-2")
                        .fullName("Jane Smith")
                        .build()
        );

        when(customerService.getCustomers(getCustomersRequest)).thenReturn(customers);

        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getCustomersPageWhenPageSizeAndPageNumberArePresentStatusShouldBeOk() throws Exception {

        var getCustomersPageRequest = GetCustomersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        var customers = List.of(
                CustomerDto.builder()
                        .id(1L)
                        .dni("123456789")
                        .phone("555-1234-1")
                        .fullName("John Doe")
                        .build()
        );

        var pagedList = new PagedList<>(customers, 1, 1, 2, 2);

        when(customerService.getCustomersPaged(getCustomersPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getCustomersPageRequest.getPageNumber().toString())
                                .param("pageSize", getCustomersPageRequest.getPageSize().toString())
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
