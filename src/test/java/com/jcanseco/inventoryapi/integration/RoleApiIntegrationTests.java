package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
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
public class RoleApiIntegrationTests {

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

    @Sql("/multiple-roles.sql")
    @Test
    public void createRoleStatusShouldBeCreated() throws JsonProcessingException {
        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Dashboard.View",
                        "Permissions.Products.Create"
                ))
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/roles", port);
    }
}
