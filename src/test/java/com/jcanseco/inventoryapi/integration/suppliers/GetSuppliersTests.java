package com.jcanseco.inventoryapi.integration.suppliers;

import com.jcanseco.inventoryapi.repositories.SupplierRepository;
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

import static com.jcanseco.inventoryapi.utils.TestModelFactory.newSupplier;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newAddress;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetSuppliersTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepository repository;

    @BeforeEach
    public void setup() {
        var suppliers = List.of(
                newSupplier("ABC Corp", "John Doe", "555-1234-1", newAddress("United States", "California", "San Francisco", "94105", "123 Main St")),
                newSupplier("XYZ Ltd", "Jane Smith", "555-1234-2",  newAddress("United Kingdom", "England", "London", "EC1A 1BB", "456 High St")),
                newSupplier("123 Enterprises", "Bob Johnson", "555-1234-3", newAddress("Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave")),
                newSupplier("Tech Solutions Inc", "Alice Brown", "555-1234-4", newAddress("Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd")),
                newSupplier("Global Innovations", "David Wilson", "555-1234-5", newAddress("Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse")),
                newSupplier("Sunshine Foods", "Linda Miller", "555-1234-6", newAddress("Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave")),
                newSupplier("Green Energy Co", "Chris Taylor", "555-1234-7", newAddress("China", "Shanghai", "Shanghai", "200000", "456 Eco Street")),
                newSupplier("Peak Performance", "Emily White", "555-1234-8", newAddress("South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road")),
                newSupplier("City Builders", "Michael Lee", "555-1234-9", newAddress("India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane")),
                newSupplier("Oceanic Ventures", "Olivia Garcia", "555-1234-10", newAddress("New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View"))
        );
        repository.saveAllAndFlush(suppliers);
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }


    @Test
    public void getSuppliersWithEmptyFiltersAndEmptyOrderByAndEmptySortOrderShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getSupplierByCompanyNameShouldReturnList() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("companyName", "a");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getSuppliersByContactNameShouldReturnList() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("contactName", "j");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getSuppliersByContactPhoneShouldReturnList() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("contactPhone", "9");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getSuppliersByCompanyAndContactInfoShouldReturnList() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("companyName", "a")
                .param("contactName", "l")
                .param("contactPhone", "5");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$").isArray());
    }
    @Test
    public void getSuppliersWhenSortOrderIsInvalidShouldReturnBadRequest() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortOrder", "invalid_sort_order");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    public void getSuppliersWhenOrderByIsInvalidShouldReturnBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("orderBy", "invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
