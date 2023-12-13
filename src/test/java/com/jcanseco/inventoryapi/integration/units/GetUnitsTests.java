package com.jcanseco.inventoryapi.integration.units;

import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetUnitsTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UnitOfMeasurementRepository repository;

    private UnitOfMeasurement createUnit(String name) {
        return UnitOfMeasurement.builder()
                .name(name)
                .build();
    }

    @BeforeEach
    public void setup() {
        var units = List.of(
                createUnit("Meter"),
                createUnit("Kilogram"),
                createUnit("Liter"),
                createUnit("Gram"),
                createUnit("Millimeter"),
                createUnit("Centimeter"),
                createUnit("Inch"),
                createUnit("Pound"),
                createUnit("Gallon"),
                createUnit("Ounce")
        );
        repository.saveAllAndFlush(units);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void getAllUnitsWhenPageNumberOrPageSizeAreNullShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "oun");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getAllUnitsWhenPageNumberAndPageSizeArePresentShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "2")
                .param("name", "l");

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
    public void getAllUnitsWhenPageNumberOrPageSizeAreNegativeStatusShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "-1")
                .param("pageSize", "1");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUnitsWhenOrderByIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("orderBy","nameee");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUnitsWhenSortOrderIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("sortOrder","descc");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
