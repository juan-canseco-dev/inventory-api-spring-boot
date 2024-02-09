package com.jcanseco.inventoryapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.controllers.UserController;
import com.jcanseco.inventoryapi.security.dtos.users.*;
import com.jcanseco.inventoryapi.security.services.JwtService;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.services.RoleService;
import com.jcanseco.inventoryapi.security.services.UserService;
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
        @MockBean(RoleService.class),
        @MockBean(JwtService.class)
})
@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

    @Test
    public void createUserWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateUserDto.builder()
                .roleId(1L)
                .fullName("Jane Doe")
                .email("jane.doe@mail.com")
                .password("jane1234")
                .build();

        when(service.createUser(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/users")
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
    public void createUserWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = CreateUserDto.builder()
                .fullName("Jane Doe")
                .email("jane.doe@mail.com")
                .password("jane1234")
                .build();

        mockMvc.perform(
                        post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserWhenModelIsValidStatusShouldBeNoContent() throws Exception {
        var dto = UpdateUserDto.builder()
                .userId(1L)
                .fullName("Jane Doe")
                .build();

        mockMvc.perform(
                        put("/api/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateUserWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = UpdateUserDto.builder()
                .userId(1L)
                .fullName(" ")
                .build();

        mockMvc.perform(
                        put("/api/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserWhenDtoIdAndPathIdAreNotEqualStatusShouldBeBadRequest() throws Exception {
        var dto = UpdateUserDto.builder()
                .userId(1L)
                .fullName("Jane Doe")
                .build();

        mockMvc.perform(
                        put("/api/users/2")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserWhenUserDoNotExistsStatusShouldBeNotFound() throws Exception {

        var dto = UpdateUserDto.builder()
                .userId(1L)
                .fullName("Jane Doe")
                .build();

        doThrow(new NotFoundException("User Not Found"))
                .when(service)
                .updateUser(dto);

        mockMvc.perform(
                        put("/api/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void changeUserRoleWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var dto = ChangeUserRoleDto.builder()
                .userId(1L)
                .roleId(1L)
                .build();

        mockMvc.perform(
                        put("/api/users/1/changeRole")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void changeUserRoleWhenModelsIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = ChangeUserRoleDto.builder()
                .userId(1L)
                .build();

        mockMvc.perform(
                        put("/api/users/1/changeRole")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeUserRoleWhenDtoIdAndPathIdAreNotEqualStatusShouldBeBadRequest() throws Exception {
        var dto = ChangeUserRoleDto.builder()
                .userId(1L)
                .roleId(1L)
                .build();

        mockMvc.perform(
                        put("/api/users/2/changeRole")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeUserRoleWhenUserDoNotExistsStatusShouldBeNotFound() throws  Exception {
        var dto = ChangeUserRoleDto.builder()
                .userId(1L)
                .roleId(1L)
                .build();

        doThrow(new NotFoundException("User Not Found"))
                .when(service)
                .changeUserRole(dto);


        mockMvc.perform(
                        put("/api/users/1/changeRole")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUserWhenUserExistsStatusShouldBeNoContent() throws Exception {
        mockMvc.perform(
                        delete("/api/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserWhenUserDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("User Not Found"))
                .when(service)
                .deleteUser(1L);

        mockMvc.perform(
                        delete("/api/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserWhenUserExistsStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void getUserWhenUserDoNotExistsStatusShouldBeNotFound() throws Exception {
        doThrow(new NotFoundException("User Not Found"))
                .when(service)
                .getUserById(1L);
        mockMvc.perform(
                        get("/api/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // Users

    @Test
    public void getUsersWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUsersWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUsersPageWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUsersWhenPageSizeAndPageNumberAreNotPresentStatusShouldBeOk() throws Exception {


        var getUsersRequest = GetUsersRequest.builder()
                .build();

        var users = List.of(
                UserDto.builder().id(1L).fullName("User 1").build(),
                UserDto.builder().id(2L).fullName("User 2").build()
        );

        when(service.getUsers(getUsersRequest)).thenReturn(users);

        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getUsersPageWhenPageSizeAndPageNumberArePresentStatusShouldBeOk() throws Exception {

        var getUsersPageRequest = GetUsersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        var users = List.of(
                UserDto.builder().id(1L).fullName("User 1").build()
        );

        var pagedList = new PagedList<>(users, 1, 1, 2, 2);

        when(service.getUsersPage(getUsersPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getUsersPageRequest.getPageNumber().toString())
                                .param("pageSize", getUsersPageRequest.getPageSize().toString())
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
