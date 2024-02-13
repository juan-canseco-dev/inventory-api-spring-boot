package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDetailsDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SupplierControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private AddressDto defaultAddressDto;

    @BeforeEach
    public void setup() {
        defaultAddressDto = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    @WithMockUser(authorities = {"Permissions.Suppliers.Create"})
    @Sql("/multiple-suppliers.sql")
    @Test
    public void createSupplierStatusShouldBeCreated() throws Exception {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddressDto)
                .build();
        mockMvc.perform(
                        post("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }


    @WithMockUser(authorities = {"Permissions.Suppliers.Update"})
    @Sql("/multiple-suppliers.sql")
    @Test
    public void updateSupplierStatusShouldBeNoContent() throws Exception {

        var supplierId = 1L;

        var address = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var dto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corporation")
                .contactName("John Doe Smith")
                .contactPhone("555-1234-7")
                .address(address)
                .build();

        mockMvc.perform(
                        put("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @WithMockUser(authorities = {"Permissions.Suppliers.Delete"})
    @Sql("/multiple-suppliers.sql")
    @Test
    public void deleteSupplierStatusShouldBeNoContent() throws Exception {
        var supplierId = 1L;
        mockMvc.perform(
                        delete("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }



    @WithMockUser(authorities = {"Permissions.Suppliers.View"})
    @Sql("/multiple-suppliers.sql")
    @Test
    public void getSupplierByIdStatusShouldBeOk() throws Exception {

        var supplierId = 1L;

        var expectedSupplier = SupplierDetailsDto.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
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

        var result = mockMvc.perform(
                        get("/api/suppliers/" + supplierId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        SupplierDetailsDto supplierResult = mapper.readValue(content, SupplierDetailsDto.class);
        assertEquals(expectedSupplier, supplierResult);
    }

    @WithMockUser(authorities = {"Permissions.Suppliers.View"})
    @Sql("/multiple-suppliers.sql")
    @Test
    public void getSuppliersStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "companyName")
                                .param("sortOrder", "asc")
                                .param("companyName", "a")
                                .param("contactName", "l")
                                .param("contactPhone", "5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @WithMockUser(authorities = {"Permissions.Suppliers.View"})
    @Sql("/multiple-suppliers.sql")
    @Test
    public void getSuppliersPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "1")
                                .param("pageSize", "2")
                                .param("orderBy", "companyName")
                                .param("sortOrder", "asc")
                                .param("companyName", "a")
                                .param("contactName", "l")
                                .param("contactPhone", "5")
                )
                .andDo(print())
                .andExpect(status().isOk())       .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
