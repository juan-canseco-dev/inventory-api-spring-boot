package com.jcanseco.inventoryapi.integration.customers;

import com.jcanseco.inventoryapi.entities.Address;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteCustomerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository repository;
    private Customer savedCustomer;

    @BeforeEach
    public void setup() {

        var address = Address
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
    public void deleteCustomerWhenCustomerExistsStatusShouldBeNoContent() throws Exception {

        var customerId = savedCustomer.getId();

        var request = MockMvcRequestBuilders
                .delete("/api/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var customerOpt = repository.findById(customerId);
        assertTrue(customerOpt.isEmpty());
    }

    @Test
    public void deleteCustomerWhenCustomerDoesNotExistsShouldBeNotFound() throws Exception {

        var customerId = 10000L;

        var request = MockMvcRequestBuilders
                .delete("/api/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
