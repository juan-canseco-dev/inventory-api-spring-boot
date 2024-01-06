package com.jcanseco.inventoryapi.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitOfMeasurementApiIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private static HttpHeaders httpHeaders;
    @BeforeAll
    public static void setup() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Sql(statements = "DELETE FROM units_of_measurement", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createUnitOfMeasurementStatusShouldBeCreated() throws JsonProcessingException {

        var dto = CreateUnitOfMeasurementDto.builder()
                .name("New Unit")
                .build();
        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    @Test
    @Sql(statements = "INSERT INTO units_of_measurement (id, name) VALUES (11, 'Pieces')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM units_of_measurement", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateUnitStatusShouldBeNoContent() throws JsonProcessingException {

        var unitId = 11L;
        var url = baseUrl() + "/" + unitId;
        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name("Piece")
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql(statements = "INSERT INTO units_of_measurement (id, name) VALUES (20, 'Each')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM units_of_measurement", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void deleteUnitStatusShouldBeNoContent() {
        var unitId = 20L;
        var url = baseUrl() + "/" + unitId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql(statements = "INSERT INTO units_of_measurement (id, name) VALUES (1, 'Piece')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM units_of_measurement", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getUnitByIdStatusShouldBeOk() {
        var unitId = 1L;
        var unitName = "Piece";
        var url = baseUrl() + "/" + unitId;
        var response = restTemplate.exchange(url, HttpMethod.GET, null, UnitOfMeasurementDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var unit = response.getBody();
        assertEquals(unitId, unit.getId());
        assertEquals(unitName, unit.getName());
    }

    @Sql("/multiple-units_of_measurement.sql")
    @Test
    public void getUnitsStatusShouldBeOk() {
        var url = baseUrl() + "?orderBy=name&sortOrder=asc&name=a";
        var responseType = new ParameterizedTypeReference<List<UnitOfMeasurementDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var units = response.getBody();
        assertEquals(3, units.size());
    }

    @Sql("/multiple-units_of_measurement.sql")
    @Test
    public void getUnitsPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=2&orderBy=name&sortOrder=asc&name=a";
        var responseType = new ParameterizedTypeReference<PagedList<UnitOfMeasurementDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var pagedList = response.getBody();
        assertEquals(1, pagedList.getPageNumber());
        assertEquals(2, pagedList.getPageSize());
        assertEquals(2, pagedList.getTotalPages());
        assertEquals(3, pagedList.getTotalElements());
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/units", port);
    }

}
