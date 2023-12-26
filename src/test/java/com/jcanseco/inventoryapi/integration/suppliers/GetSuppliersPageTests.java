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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetSuppliersPageTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepository repository;

    @BeforeEach
    public void setup() {
        var suppliers = List.of(
                newSupplier("ABC Corp", "John Doe", "555-1234-1", newSupplierAddress("United States", "California", "San Francisco", "94105", "123 Main St")),
                newSupplier("XYZ Ltd", "Jane Smith", "555-1234-2",  newSupplierAddress("United Kingdom", "England", "London", "EC1A 1BB", "456 High St")),
                newSupplier("123 Enterprises", "Bob Johnson", "555-1234-3", newSupplierAddress("Canada", "Ontario", "Toronto", "M5V 2A8", "789 Maple Ave")),
                newSupplier("Tech Solutions Inc", "Alice Brown", "555-1234-4", newSupplierAddress("Australia", "New South Wales", "Sydney", "2000", "101 Tech Blvd")),
                newSupplier("Global Innovations", "David Wilson", "555-1234-5", newSupplierAddress("Germany", "Bavaria", "Munich", "80331", "234 Innovation Strasse")),
                newSupplier("Sunshine Foods", "Linda Miller", "555-1234-6", newSupplierAddress("Brazil", "Sao Paulo", "Sao Paulo", "01310-200", "345 Sunny Ave")),
                newSupplier("Green Energy Co", "Chris Taylor", "555-1234-7", newSupplierAddress("China", "Shanghai", "Shanghai", "200000", "456 Eco Street")),
                newSupplier("Peak Performance", "Emily White", "555-1234-8", newSupplierAddress("South Africa", "Gauteng", "Johannesburg", "2000", "567 Summit Road")),
                newSupplier("City Builders", "Michael Lee", "555-1234-9", newSupplierAddress("India", "Maharashtra", "Mumbai", "400001", "678 Urban Lane")),
                newSupplier("Oceanic Ventures", "Olivia Garcia", "555-1234-10", newSupplierAddress("New Zealand", "Auckland", "Auckland", "1010", "789 Ocean View"))
        );
        repository.saveAllAndFlush(suppliers);
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void getSuppliersPageWithEmptyFiltersAndEmptyOrderByAndEmptySortOrderShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
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
    public void getSuppliersPageByCompanyNameShouldReturnPagedList() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("companyName", "a")
                .param("pageNumber", "1")
                .param("pageSize", "2");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }

    @Test
    public void getSuppliersPageByContactNameShouldReturnPagedList() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("contactName", "j")
                .param("pageNumber", "1")
                .param("pageSize", "2");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }

    @Test
    public void getSuppliersPageByContactPhoneShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("contactPhone", "1234-1")
                .param("pageNumber", "1")
                .param("pageSize", "1");

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
    public void getSuppliersPageByCompanyAndContactInfoShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("companyName", "a")
                .param("contactName", "l")
                .param("contactPhone", "5")
                .param("pageNumber", "1")
                .param("pageSize", "2");


        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
    @Test
    public void getSuppliersPageWhenSortOrderIsInvalidShouldReturnBadRequest() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "5")
                .param("sortOrder", "invalid_sort_order");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
    @Test
    public void getSuppliersPageWhenOrderByIsInvalidShouldReturnBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "5")
                .param("orderBy", "invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

}
