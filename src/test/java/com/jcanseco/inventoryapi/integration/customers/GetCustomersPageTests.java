package com.jcanseco.inventoryapi.integration.customers;

import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.CustomerAddress;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetCustomersPageTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository repository;
    private Customer createCustomer(String dni, String phone, String fullName, CustomerAddress address) {
        return Customer.builder()
                .dni(dni)
                .phone(phone)
                .fullName(fullName)
                .address(address)
                .build();
    }

    private CustomerAddress createCustomerAddress(String country, String state, String city, String zipCode, String street) {
        return CustomerAddress.builder()
                .country(country)
                .state(state)
                .city(city)
                .zipCode(zipCode)
                .street(street)
                .build();
    }

    @BeforeEach
    public void setup() {
        var customers = List.of(
                createCustomer("123456789", "555-1234-1", "John Doe", createCustomerAddress("United States", "California", "San Francisco", "94105", "123 Main St")),
                createCustomer("987654321", "555-1234-2", "Jane Smith", createCustomerAddress("United Kingdom", "England", "London", "EC1A 1BB", "456 High St")),
                createCustomer("456789012", "555-1234-3", "Bob Johnson", createCustomerAddress("Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave")),
                createCustomer("789012345", "555-1234-4", "Alice Brown", createCustomerAddress("Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd")),
                createCustomer("234567890", "555-1234-5", "David Wilson", createCustomerAddress("Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse")),
                createCustomer("567890123", "555-1234-6", "Linda Miller", createCustomerAddress("Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave")),
                createCustomer("890123456", "555-1234-7", "Chris Taylor", createCustomerAddress("China", "Shanghai", "Shanghai", "200000", "456 Eco Street")),
                createCustomer("345678901", "555-1234-8", "Emily White", createCustomerAddress("South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road")),
                createCustomer("678901234", "555-1234-9", "Michael Lee", createCustomerAddress("India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane")),
                createCustomer("901234567", "555-1234-10", "Olivia Garcia", createCustomerAddress("New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View"))
        );
        repository.saveAllAndFlush(customers);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void getCustomersPageWithEmptyFilterAndEmptyOrderAndEmptySortOrderShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "5");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(5))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }

    @Test
    public void getCustomersPageByDniShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "3")
                .param("dni", "12");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(3))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(7))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }

    @Test
    public void getCustomersPageByPhoneShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "1")
                .param("phone", "1234-1");

        mockMvc.perform(request)
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

    @Test
    public void getCustomersPageByFullNameShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "1")
                .param("fullName", "john");

        mockMvc.perform(request)
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

    @Test
    public void getCustomersPageByAllFiltersShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "2")
                .param("dni", "1")
                .param("phone", "555")
                .param("fullName", "o");

        mockMvc.perform(request)
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

    @Test
    public void getCustomersWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "10")
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
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("orderBy", "invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

}
