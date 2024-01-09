package com.jcanseco.inventoryapi.integration.customers;

import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import java.util.List;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newCustomer;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newAddress;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetCustomersTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository repository;
    @BeforeEach
    public void setup() {
        var customers = List.of(
                newCustomer("123456789", "555-1234-1", "John Doe", newAddress("United States", "California", "San Francisco", "94105", "123 Main St")),
                newCustomer("987654321", "555-1234-2", "Jane Smith", newAddress("United Kingdom", "England", "London", "EC1A 1BB", "456 High St")),
                newCustomer("456789012", "555-1234-3", "Bob Johnson", newAddress("Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave")),
                newCustomer("789012345", "555-1234-4", "Alice Brown", newAddress("Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd")),
                newCustomer("234567890", "555-1234-5", "David Wilson", newAddress("Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse")),
                newCustomer("567890123", "555-1234-6", "Linda Miller", newAddress("Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave")),
                newCustomer("890123456", "555-1234-7", "Chris Taylor", newAddress("China", "Shanghai", "Shanghai", "200000", "456 Eco Street")),
                newCustomer("345678901", "555-1234-8", "Emily White", newAddress("South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road")),
                newCustomer("678901234", "555-1234-9", "Michael Lee", newAddress("India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane")),
                newCustomer("901234567", "555-1234-10", "Olivia Garcia", newAddress("New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View"))
        );
        repository.saveAllAndFlush(customers);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void getCustomersWithEmptyFilterAndEmptyOrderAndEmptySortOrderShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCustomerByDniShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("dni", "12");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(7))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCustomerByPhoneShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("phone", "1234-1");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCustomerByFullNameShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("fullName", "john");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCustomersByAllFiltersShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("dni", "1")
                .param("phone", "555")
                .param("fullName", "o");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void getCustomersWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortOrder", "invalid_sort_order");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCustomersWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("orderBy", "invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

}
