package com.jcanseco.inventoryapi.integration.units;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteUnitTests {
    @Autowired
    private MockMvc mockMvc;

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
    public void deleteUnitWhenUnitExistsStatusShouldBeOk() throws Exception {

        var unitId = savedUnit.getId();

        var request = MockMvcRequestBuilders
                .delete("/api/units/" + unitId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var unitOpt = repository.findById(unitId);
        assertTrue(unitOpt.isEmpty());
    }


    @Test
    public void deleteUnitWhenUnitNotExistsStatusShouldBeNotFound() throws Exception {
        var unitId = 5000L;

        var request = MockMvcRequestBuilders
                .delete("/api/units/" + unitId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

}
