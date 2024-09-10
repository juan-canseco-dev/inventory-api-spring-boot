package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDetailsDto;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private static AddressDto defaultAddressDto;

    @BeforeAll
    public static void setup() {
        defaultAddressDto = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }


    @WithMockUser(authorities = {"Permissions.Customers.Create"})
    @Sql("/multiple-customers.sql")
    @Test
    public void createCustomerStatusShouldBeCreated() throws Exception {
        var dto = CreateCustomerDto.builder()
                .dni("12345678912")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddressDto)
                .build();
        mockMvc.perform(
                        post("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }

    @WithMockUser()
    @Test
    public void createCustomerWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
        var dto = CreateCustomerDto.builder()
                .dni("12345678912")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddressDto)
                .build();
        mockMvc.perform(
                        post("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void createCustomerWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        var dto = CreateCustomerDto.builder()
                .dni("12345678912")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddressDto)
                .build();
        mockMvc.perform(
                        post("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"Permissions.Customers.Update"})
    @Sql("/multiple-customers.sql")
    @Test
    public void updateCustomerStatusShouldBeNoContent() throws Exception {

        var customerId = 1L;
        var dto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName("John Doe Smith")
                .phone("555-1234-9")
                .address(defaultAddressDto)
                .build();

        mockMvc.perform(
                        put("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser()
    @Test
    public void updateCustomerWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
        var customerId = 1L;
        var dto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName("John Doe Smith")
                .phone("555-1234-9")
                .address(defaultAddressDto)
                .build();

        mockMvc.perform(
                        put("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateCustomerWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        var customerId = 1L;
        var dto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName("John Doe Smith")
                .phone("555-1234-9")
                .address(defaultAddressDto)
                .build();

        mockMvc.perform(
                        put("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"Permissions.Customers.Delete"})
    @Sql("/multiple-customers.sql")
    @Test
    public void deleteCustomerStatusShouldBeNoContent() throws Exception  {
        var customerId = 1L;
        mockMvc.perform(
                        delete("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser()
    @Test
    public void deleteCustomerWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
        var customerId = 1L;
        mockMvc.perform(
                        delete("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteCustomerWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        var customerId = 1L;
        mockMvc.perform(
                        delete("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"Permissions.Customers.View"})
    @Sql("/multiple-customers.sql")
    @Test
    public void getCustomerByIdStatusShouldBeNoContent() throws Exception {
        var customerId = 1L;
        var expected = CustomerDetailsDto.builder()
                .id(customerId)
                .dni("123456789")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(
                        AddressDto.builder()
                                .country("United States")
                                .state("California")
                                .city("San Francisco")
                                .zipCode("94105")
                                .street("123 Main St")
                                .build()
                )
                .build();

        mockMvc.perform(
                        get("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.dni").value(expected.getDni()))
                .andExpect(jsonPath("$.phone").value(expected.getPhone()))
                .andExpect(jsonPath("$.fullName").value(expected.getFullName()))
                .andExpect(jsonPath("$.address").value(expected.getAddress()));
    }

    @WithMockUser
    @Test
    public void getCustomerByIdWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
        var customerId = 1L;
        mockMvc.perform(
                        get("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getCustomerByIdWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        var customerId = 1L;
        mockMvc.perform(
                        get("/api/customers/" + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(authorities = {"Permissions.Customers.View"})
    @Sql("/multiple-customers.sql")
    @Test
    public void getCustomersStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "fullName")
                                .param("sortOrder", "asc")
                                .param("dni", "1")
                                .param("phone", "555")
                                .param("fullName", "o")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @WithMockUser(authorities = {"Permissions.Customers.View"})
    @Sql("/multiple-customers.sql")
    @Test
    public void getCustomersPageShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "1")
                                .param("pageSize", "2")
                                .param("orderBy", "fullName")
                                .param("sortOrder", "asc")
                                .param("dni", "1")
                                .param("phone", "555")
                                .param("fullName", "o")

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

    @WithMockUser
    @Test
    public void getCustomersWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getCustomerWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        mockMvc.perform(
                        get("/api/customers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
