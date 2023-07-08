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
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UnitOfMeasurementRepository repository;
    private UnitOfMeasurement savedUnit;

    @BeforeEach
    public void setup() {
        savedUnit = repository.saveAndFlush( UnitOfMeasurement.builder().name("Kilogram").build());
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void getUnitByIdWhenUnitExistsStatusShouldBeOk() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units/" + savedUnit.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id").value(savedUnit.getId()))
                .andExpect(jsonPath("$.name").value(savedUnit.getName()));
    }

    @Test
    public void getUnitByIdWhenUnitNotExistsStatusShouldBeNotFound() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/units/1000")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
