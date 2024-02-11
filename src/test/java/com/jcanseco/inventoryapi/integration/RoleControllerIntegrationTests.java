package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.roles.UpdateRoleDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoleControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @WithMockUser
    @Sql("/multiple-roles.sql")
    @Test
    public void createRoleStatusShouldBeCreated() throws Exception {
        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Dashboard.View"
                ))
                .build();

        mockMvc.perform(
                        post("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }

    @WithMockUser
    @Sql("/multiple-roles.sql")
    @Test
    public void updateRoleStatusShouldBeNoContent() throws Exception {
        var roleId = 1L;
        var dto = UpdateRoleDto.builder()
                .roleId(roleId)
                .name("Updated Role")
                .permissions(List.of(
                        "Permissions.Dashboard.View",
                        "Permissions.Categories.View"
                ))
                .build();

        mockMvc.perform(
                        put("/api/roles/" + roleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Sql("/multiple-roles.sql")
    @Test
    public void deleteRoleStatusShouldBeNoContent() throws Exception {
        var roleId = 1L;
        mockMvc.perform(
                        delete("/api/roles/" + roleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Sql("/multiple-roles.sql")
    @Test
    public void getRoleByIdStatusShouldBeOk() throws Exception {
        var roleId = 1L;
        var expectedRole = RoleDetailsDto.builder()
                .id(roleId)
                .name("Dashboard")
                .permissions(List.of("Permissions.Dashboard.View"))
                .createdAt(LocalDateTime.of(2023, Month.MAY, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2023, Month.MAY, 2, 0, 0))
                .build();

        var result = mockMvc.perform(
                        get("/api/roles/" + roleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        RoleDetailsDto roleResult = mapper.readValue(content, RoleDetailsDto.class);
        assertEquals(expectedRole, roleResult);

    }

    @WithMockUser
    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "name")
                                .param("sortOrder", "asc")
                                .param("name", "p")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @WithMockUser
    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "1")
                                .param("pageSize", "1")
                                .param("orderBy", "name")
                                .param("sortOrder", "asc")
                                .param("name", "p")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
