package com.jcanseco.inventoryapi.integration.units;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UnitOfMeasurementRepository repository;

    private UnitOfMeasurement savedUnit;

    @BeforeEach
    public void setup() {
        savedUnit = repository.saveAndFlush(UnitOfMeasurement.builder().name("Kilogram").build());
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void updateUnitWhenUnitExistsAndModelIsValidStatusShouldBeOk() throws Exception {

        var unitId = savedUnit.getId();
        var updatedUnit = new UpdateUnitOfMeasurementDto(unitId, "Kilo");


        var request = MockMvcRequestBuilders
                .put("/api/units/" + unitId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUnit));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id").value(unitId))
                .andExpect(jsonPath("$.name").value(updatedUnit.getName()));
    }

    @Test
    public void updateUnitWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var unitId = savedUnit.getId();
        var updatedUnit = new UpdateUnitOfMeasurementDto(unitId, "");


        var request = MockMvcRequestBuilders
                .put("/api/units/" + unitId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUnit));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUnitWhenUnitDoesNotExistsStatusShouldBeNotFound() throws Exception {

        var unitId = 20000L;
        var updatedUnit = new UpdateUnitOfMeasurementDto(unitId, "Kilo");


        var request = MockMvcRequestBuilders
                .put("/api/units/" + unitId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUnit));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
