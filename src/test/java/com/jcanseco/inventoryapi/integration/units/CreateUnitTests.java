package com.jcanseco.inventoryapi.integration.units;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import org.junit.jupiter.api.AfterEach;
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
public class CreateUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UnitOfMeasurementRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createUnitWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var createdDto = new CreateUnitOfMeasurementDto("Kilogram");

        var request = MockMvcRequestBuilders
                .post("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createdDto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void createUnitWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception{
        var createdDto = new CreateUnitOfMeasurementDto("");

        var request = MockMvcRequestBuilders
                .post("/api/units")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createdDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
