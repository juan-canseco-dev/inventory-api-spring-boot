package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.users.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class UserApiIntegrationTests {
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

    @Sql("/multiple-users.sql")
    @Test
    public void createUserStatusShouldBeCreated() throws JsonProcessingException {
        var dto = CreateUserDto.builder()
                .roleId(1L)
                .email("rusty.smith@mail.com")
                .fullName("Rusty Smith")
                .password("rusty.password.1234")
                .build();
        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    @Sql("/multiple-users.sql")
    @Test
    public void updateUserStatusShouldBeNoContent() throws JsonProcessingException {
        var userId = 1L;
        var dto = UpdateUserDto.builder()
                .userId(userId)
                .fullName("John Doe Smith")
                .build();
        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl() + "/" + userId, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-users.sql")
    @Test
    public void changeUserRoleStatusShouldBeNoContent() throws JsonProcessingException {

        var userId = 1L;
        var roleId = 2L;

        var dto = ChangeUserRoleDto.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl() + "/" + userId + "/changeRole", HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-users.sql")
    @Test
    public void deleteUserStatusShouldBeNoContent() {
        var response = restTemplate.exchange(baseUrl() + "/1" , HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-users.sql")
    @Test
    public void getUserStatusShouldBeOk() {

        var userId = 1L;

        var expectedRole = RoleDetailsDto.builder()
                .id(1L)
                .name("Dashboard")
                .createdAt(LocalDateTime.of(2023, Month.MAY, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2023, Month.MAY, 2, 0, 0))
                .permissions(List.of("Permissions.Dashboard.View"))
                .build();

        var expectedUser = UserDetailsDto.builder()
                .id(userId)
                .fullName("John Doe")
                .email("john_doe@gmail.com")
                .role(expectedRole)
                .createdAt(LocalDateTime.of(2023, Month.MAY, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2023, Month.MAY, 2, 0, 0))
                .build();

        var url = baseUrl() + "/" + userId;
        var response = restTemplate.exchange(url, HttpMethod.GET, null, UserDetailsDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var user = response.getBody();
        assertEquals(expectedUser, user);
    }

    @Sql("/multiple-users.sql")
    @Test
    public void getUsersStatusShouldBeOk() throws JsonProcessingException {
        var url = baseUrl() + "?orderBy=fullName&sortOrder=asc&fullName=doe";
        var responseType = new ParameterizedTypeReference<List<UserDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var users = response.getBody();
        assertEquals(2, users.size());
    }

    @Sql("/multiple-users.sql")
    @Test
    public void getUsersPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=1&orderBy=fullName&sortOrder=asc&fullName=doe&email=doe";
        var responseType = new ParameterizedTypeReference<PagedList<UserDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var pagedList = response.getBody();
        assertEquals(1, pagedList.getPageNumber());
        assertEquals(1, pagedList.getPageSize());
        assertEquals(2, pagedList.getTotalPages());
        assertEquals(2, pagedList.getTotalElements());
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/users", port);
    }
}
