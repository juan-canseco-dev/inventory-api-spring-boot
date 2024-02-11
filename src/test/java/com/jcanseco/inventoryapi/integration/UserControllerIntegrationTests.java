package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.users.*;
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
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @WithMockUser
    @Sql("/multiple-users.sql")
    @Test
    public void createUserStatusShouldBeCreated() throws Exception {
        var dto = CreateUserDto.builder()
                .roleId(1L)
                .email("rusty.smith@mail.com")
                .fullName("Rusty Smith")
                .password("rusty.password.1234")
                .build();
        mockMvc.perform(
                        post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }

    @WithMockUser
    @Sql("/multiple-users.sql")
    @Test
    public void updateUserStatusShouldBeNoContent() throws Exception {
        var userId = 1L;
        var dto = UpdateUserDto.builder()
                .userId(userId)
                .fullName("John Doe Smith")
                .build();
        mockMvc.perform(
                        put("/api/users/" + userId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Sql("/multiple-users.sql")
    @Test
    public void changeUserRoleStatusShouldBeNoContent() throws Exception {

        var userId = 1L;
        var roleId = 2L;

        var dto = ChangeUserRoleDto.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        mockMvc.perform(
                        put("/api/users/" + userId + "/changeRole")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Sql("/multiple-users.sql")
    @Test
    public void deleteUserStatusShouldBeNoContent() throws Exception {
        var userId = 1L;
        mockMvc.perform(
                        delete("/api/users/" + userId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Sql("/multiple-users.sql")
    @Test
    public void getUserStatusShouldBeOk() throws Exception {

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

        var result = mockMvc.perform(
                        get("/api/users/" + userId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        UserDetailsDto userResult = mapper.readValue(content, UserDetailsDto.class);
        assertEquals(expectedUser, userResult);
    }

    @WithMockUser
    @Sql("/multiple-users.sql")
    @Test
    public void getUsersStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "fullName")
                                .param("sortOrder", "asc")
                                .param("fullName", "doe")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockUser
    @Sql("/multiple-users.sql")
    @Test
    public void getUsersPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "1")
                                .param("pageSize", "1")
                                .param("orderBy", "fullName")
                                .param("sortOrder", "asc")
                                .param("fullName", "doe")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
