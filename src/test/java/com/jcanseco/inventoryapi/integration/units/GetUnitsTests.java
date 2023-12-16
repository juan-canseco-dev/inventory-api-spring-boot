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
    public void getUnitsWithNameFilterShouldReturnList() throws Exception {

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
    public void getUnitsWhenOrderByIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("orderBy","invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUnitsWhenSortOrderIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("sortOrder","sort_order_invalid");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
