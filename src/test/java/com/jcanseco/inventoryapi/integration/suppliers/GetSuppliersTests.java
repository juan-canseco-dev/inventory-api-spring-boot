package com.jcanseco.inventoryapi.integration.suppliers;

import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetSuppliersTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepository repository;

    private Supplier createSupplier(String companyName,
                                    String contactName,
                                    String contactPhone,
                                    String country,
                                    String state,
                                    String city,
                                    String zipCode,
                                    String street) {
        return Supplier.builder()
                .companyName(companyName)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .address(SupplierAddress.builder()
                        .country(country)
                        .state(state)
                        .city(city)
                        .zipCode(zipCode)
                        .street(street)
                        .build())
                .build();
    }

    @BeforeEach
    public void setup() {
        var suppliers = List.of(
                createSupplier("ABC Corp", "John Doe", "555-1234-1", "United States", "California", "San Francisco", "94105", "123 Main St"),
                createSupplier("XYZ Ltd", "Jane Smith", "555-1234-2", "United Kingdom", "England", "London", "EC1A 1BB", "456 High St"),
                createSupplier("123 Enterprises", "Bob Johnson", "555-1234-3", "Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave"),
                createSupplier("Tech Solutions Inc", "Alice Brown", "555-1234-4", "Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd"),
                createSupplier("Global Innovations", "David Wilson", "555-1234-5", "Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse"),
                createSupplier("Sunshine Foods", "Linda Miller", "555-1234-6", "Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave"),
                createSupplier("Green Energy Co", "Chris Taylor", "555-1234-7", "China", "Shanghai", "Shanghai", "200000", "456 Eco Street"),
                createSupplier("Peak Performance", "Emily White", "555-1234-8", "South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road"),
                createSupplier("City Builders", "Michael Lee", "555-1234-9", "India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane"),
                createSupplier("Oceanic Ventures", "Olivia Garcia", "555-1234-10", "New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View")
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
