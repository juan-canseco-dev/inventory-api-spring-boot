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

    @BeforeEach
    public void setup() {
        var categories = List.of(
                UnitOfMeasurement.builder().name("Kilogram").build(),
                UnitOfMeasurement.builder().name("Kilo").build(),
                UnitOfMeasurement.builder().name("Box").build()
        );
        repository.saveAllAndFlush(categories);
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
                .param("name", "k");

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
                .param("pageSize", "1")
                .param("name", "b");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.content", Matchers.notNullValue()))
                .andExpect(jsonPath("$.content.size()").value(1));
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
}
