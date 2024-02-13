package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UnitOfMeasurementControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @WithMockUser(authorities = {"Permissions.UnitsOfMeasurement.Create"})
    @Test
    @Sql("/multiple-units_of_measurement.sql")
    public void createUnitOfMeasurementStatusShouldBeCreated() throws Exception {

        var dto = CreateUnitOfMeasurementDto.builder()
                .name("New Unit")
                .build();
        mockMvc.perform(
                        post("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }


    @WithMockUser(authorities = {"Permissions.UnitsOfMeasurement.Update"})
    @Test
    @Sql("/multiple-units_of_measurement.sql")
    public void updateUnitStatusShouldBeNoContent() throws Exception {

        var unitId = 1L;
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name("Meter MT")
                .build();

        mockMvc.perform(
                        put("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser(authorities = {"Permissions.UnitsOfMeasurement.Delete"})
    @Sql("/multiple-units_of_measurement.sql")
    @Test
    public void deleteUnitStatusShouldBeNoContent() throws Exception {
        var unitId = 1L;
        mockMvc.perform(
                        delete("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @WithMockUser(authorities = {"Permissions.UnitsOfMeasurement.View"})
    @Sql("/multiple-units_of_measurement.sql")
    @Test
    public void getUnitByIdStatusShouldBeOk() throws Exception {

        var unitId = 1L;
        var expectedName = "Meter";

        mockMvc.perform(
                        get("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(unitId))
                .andExpect(jsonPath("$.name").value(expectedName));
    }


    @WithMockUser(authorities = {"Permissions.UnitsOfMeasurement.View"})
    @Sql("/multiple-units_of_measurement.sql")
    @Test
    public void getUnitsStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "name")
                                .param("sortOrder", "asc")
                                .param("name", "a")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @WithMockUser(authorities = {"Permissions.UnitsOfMeasurement.View"})
    @Sql("/multiple-units_of_measurement.sql")
    @Test
    public void getUnitsPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "name")
                                .param("sortOrder", "asc")
                                .param("name", "a")
                                .param("pageNumber", "1")
                                .param("pageSize", "2")
                )
                .andDo(print())
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
}
