package com.jcanseco.inventoryapi.integration.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateCustomerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CustomerRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createCustomerWhenModelIsValidStatusShouldBeOk() throws Exception {

        var address = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var createDto = CreateCustomerDto.builder()
                .dni("901234567")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(address)
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.dni", Matchers.notNullValue()))
                .andExpect(jsonPath("$.phone", Matchers.notNullValue()))
                .andExpect(jsonPath("$.fullName", Matchers.notNullValue()))
                .andExpect(jsonPath("$.address", Matchers.notNullValue()));
    }

    @Test
    public void createCustomerWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var createDto = CreateCustomerDto.builder()
                .dni("901234567")
                .phone("555-1234-1")
                .fullName("John Doe")
                .build();

        var request = MockMvcRequestBuilders
                .post("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
