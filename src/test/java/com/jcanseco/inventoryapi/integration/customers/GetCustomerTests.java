package com.jcanseco.inventoryapi.integration.customers;

import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.CustomerAddress;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetCustomerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository repository;
    private Customer savedCustomer;

    @BeforeEach
    public void setup() {

        var address = CustomerAddress
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var customer = Customer.builder()
                .dni("90123456754")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(address)
                .build();

        savedCustomer = repository.saveAndFlush(customer);
    }

    @AfterEach
    public void cleanup () {
        repository.deleteAll();
    }

    @Test
    public void getCustomerWhenCustomerExistsStatusShouldBeOk() throws Exception {

        var customerId = savedCustomer.getId();

        var request = MockMvcRequestBuilders
                .get("/api/customers/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.dni").value(savedCustomer.getDni()))
                .andExpect(jsonPath("$.phone").value(savedCustomer.getPhone()))
                .andExpect(jsonPath("$.fullName").value(savedCustomer.getFullName()))
                .andExpect(jsonPath("$.address", Matchers.notNullValue()))
                .andExpect(jsonPath("$.address.country").value(savedCustomer.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.state").value(savedCustomer.getAddress().getState()))
                .andExpect(jsonPath("$.address.city").value(savedCustomer.getAddress().getCity()))
                .andExpect(jsonPath("$.address.zipCode").value(savedCustomer.getAddress().getZipCode()))
                .andExpect(jsonPath("$.address.street").value(savedCustomer.getAddress().getStreet()));
    }

    @Test
    public void getCustomerWhenCustomerNotExistsStatusShouldBeNotFound() throws Exception {

        var customerId = 10000L;

        var request = MockMvcRequestBuilders
                .get("/api/customers/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
