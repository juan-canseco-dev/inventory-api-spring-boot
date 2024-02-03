package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.UpdateRoleDto;
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
import java.time.LocalDateTime;
import java.time.Month;
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
                        "Permissions.Dashboard.View"
                ))
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void updateRoleStatusShouldBeNoContent() throws JsonProcessingException {
        var roleId = 1L;
        var dto = UpdateRoleDto.builder()
                .roleId(roleId)
                .name("Updated Role")
                .permissions(List.of(
                        "Permissions.Dashboard.View",
                        "Permissions.Categories.View"
                ))
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl() + "/" + roleId, HttpMethod.PUT, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void deleteRoleStatusShouldBeNoContent() {
        var roleId = 1L;
        var response = restTemplate.exchange(baseUrl() + "/" + roleId, HttpMethod.DELETE, null, Long.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRoleByIdStatusShouldBeOk() {
        var roleId = 1L;
        var expectedRole = RoleDetailsDto.builder()
                .id(roleId)
                .name("Dashboard")
                .permissions(List.of("Permissions.Dashboard.View"))
                .createdAt(LocalDateTime.of(2023, Month.MAY, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2023, Month.MAY, 2, 0, 0))
                .build();
        var url = baseUrl() + "/" + roleId;
        var response = restTemplate.exchange(url, HttpMethod.GET, null, RoleDetailsDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var role = response.getBody();
        assertEquals(expectedRole, role);
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesStatusShouldBeOk() {
        var url = baseUrl() + "?orderBy=name&sortOrder=asc&name=p";
        var responseType = new ParameterizedTypeReference<List<RoleDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var roles = response.getBody();
        assertEquals(3, roles.size());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=1&orderBy=name&sortOrder=asc&name=p";
        var responseType = new ParameterizedTypeReference<PagedList<RoleDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var pagedList = response.getBody();
        assertEquals(1, pagedList.getPageNumber());
        assertEquals(1, pagedList.getPageSize());
        assertEquals(3, pagedList.getTotalPages());
        assertEquals(3, pagedList.getTotalElements());
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/roles", port);
    }
}
