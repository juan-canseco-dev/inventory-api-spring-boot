package com.jcanseco.inventoryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.controllers.RoleController;
import com.jcanseco.inventoryapi.security.dtos.roles.*;
import com.jcanseco.inventoryapi.security.services.*;
import com.jcanseco.inventoryapi.services.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
        @MockBean(CustomerService.class),
        @MockBean(CategoryService.class),
        @MockBean(ProductService.class),
        @MockBean(SupplierService.class),
        @MockBean(UnitService.class),
        @MockBean(PurchaseService.class),
        @MockBean(OrderService.class),
        @MockBean(ResourceService.class),
        @MockBean(UserService.class),
        @MockBean(JwtService.class),
        @MockBean(AuthenticationService.class)
})
@WebMvcTest(
        controllers = RoleController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class RoleControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    @MockBean
    private RoleService service;


    @Test
    public void createRoleWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Users.View"
                ))
                .build();

        when(service.createRole(dto)).thenReturn(1L);
        mockMvc.perform(
                        post("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber())
                .andExpect(jsonPath("$").value(1L));

    }

    @Test
    public void createRoleWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Users.View",
                        "Permissions.Users.View"
                ))
                .build();

        mockMvc.perform(
                        post("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void updateRoleWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var dto = UpdateRoleDto.builder()
                .roleId(1L)
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Users.View"
                ))
                .build();

        mockMvc.perform(
                        put("/api/roles/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateRoleWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = UpdateRoleDto.builder()
                .roleId(1L)
                .name("")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Users.View"
                ))
                .build();

        mockMvc.perform(
                        put("/api/roles/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateRoleWhenRoleIsNotFoundStatusShouldBeNotFound() throws Exception{
        var dto = UpdateRoleDto.builder()
                .roleId(1L)
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Users.View"
                ))
                .build();

        doThrow(new NotFoundException("Role Not Found"))
                .when(service)
                .updateRole(dto);

        mockMvc.perform(
                        put("/api/roles/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRoleWhenDtoIdAndPathIdAreNotEqualStatusShouldBeBadRequest() throws Exception {
        var dto = UpdateRoleDto.builder()
                .roleId(1L)
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Users.View"
                ))
                .build();


        mockMvc.perform(
                        put("/api/roles/11")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteRoleWhenRoleExistsStatusShouldBeNoContent() throws Exception {
        mockMvc.perform(
                        delete("/api/roles/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteRoleWhenRoleNotExistsStatusShouldBeNotFound() throws Exception {

        doThrow(new NotFoundException("Role Not Found"))
                .when(service)
                .deleteRole(1L);

        mockMvc.perform(
                        delete("/api/roles/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void getRoleIdWhenRoleExistsStatusShouldBeOk() throws Exception {

        var roleId = 1L;

        var role = RoleDetailsDto.builder()
                .id(roleId)
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Roles.View",
                        "Permissions.Users.View"
                ))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusDays(2))
                .build();

        when(service.getRoleById(roleId)).thenReturn(role);

        mockMvc.perform(
                        get("/api/roles/" + roleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getRolesWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRolesWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRolesPageWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRolesWhenPageSizeAndPageNumberAreNotPresentStatusShouldBeOk() throws Exception {


        var getRolesRequest = GetRolesRequest.builder()
                .build();

        var roles = List.of(
                RoleDto.builder().id(1L).name("Role 1").build(),
                RoleDto.builder().id(2L).name("Role 2").build()
        );

        when(service.getRoles(getRolesRequest)).thenReturn(roles);

        mockMvc.perform(
                        get("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getRolesPageWhenPageSizeAndPageNumberArePresentStatusShouldBeOk() throws Exception {

        var getRolesPageRequest = GetRolesRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        var roles = List.of(
                RoleDto.builder().id(1L).name("Role 1").build()
        );

        var pagedList = new PagedList<>(roles, 1, 1, 2, 2);

        when(service.getRolesPage(getRolesPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/roles")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getRolesPageRequest.getPageNumber().toString())
                                .param("pageSize", getRolesPageRequest.getPageSize().toString())
                )
                .andDo(print())
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
