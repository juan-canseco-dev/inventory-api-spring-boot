package com.jcanseco.inventoryapi.integration.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.CustomerAddress;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateCustomerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CustomerRepository repository;
    private Customer savedCustomer;

    @BeforeEach
    public void setup() {

        var address = CustomerAddress
                .builder()
                .country("MX")
                .state("SON")
                .city("HMO")
                .zipCode("8")
                .street("CENT")
                .build();

        var customer = Customer.builder()
                .dni("90123456754")
                .phone("555-1234-0")
                .fullName("John")
                .address(address)
                .build();

        savedCustomer = repository.saveAndFlush(customer);
    }

    @AfterEach
    public void cleanup () {
        repository.deleteAll();
    }

    @Test
    public void updateCustomerWhenCustomerExistsAndModelIsValidStatusShouldBeOk() throws Exception {

        var customerId = savedCustomer.getId();

        var address = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var updateDto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("901234567")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(address)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/customers/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateCustomerWhenCustomerExistsAndModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var customerId = savedCustomer.getId();

        var updateDto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("901234567")
                .phone("555-1234-1")
                .fullName("John Doe")
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/customers/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCustomerWhenCustomerDoesNotExistsStatusShouldBeNotFound() throws Exception {

        var customerId = 10000L;

        var address = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var updateDto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("901234567")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(address)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/customers/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
