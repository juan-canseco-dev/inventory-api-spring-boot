package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
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
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @WithMockUser(authorities = {"Permissions.Categories.Create"})
    @Test
    @Sql("/multiple-categories.sql")
    public void createCategoryStatusShouldBeCreated() throws Exception {

        var dto = CreateCategoryDto.builder()
                .name("New Category")
                .build();
        mockMvc.perform(
                        post("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }

    @WithMockUser(authorities = {"Permissions.Categories.Update"})
    @Test
    @Sql("/multiple-categories.sql")
    public void updateCategoryStatusShouldBeNoContent() throws Exception {
        var categoryId = 1L;
        var dto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name("Computers & Electronics")
                .build();

        mockMvc.perform(
                        put("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser(authorities = {"Permissions.Categories.Delete"})
    @Test
    @Sql("/multiple-categories.sql")
    public void deleteCategoryStatusShouldBeNoContent() throws Exception {
        var categoryId = 1L;
        mockMvc.perform(
                        delete("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @WithMockUser(authorities = {"Permissions.Categories.View"})
    @Sql("/multiple-categories.sql")
    @Test
    public void getCategoryByIdStatusShouldBeOk() throws Exception {
        var categoryId = 1L;
        var expectedName = "Electronics";

        mockMvc.perform(
                        get("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(expectedName));
    }

    @WithMockUser(authorities = {"Permissions.Categories.View"})
    @Sql("/multiple-categories.sql")
    @Test
    public void getCategoriesStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "name")
                                .param("sortOrder", "asc")
                                .param("name", "a")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @WithMockUser(authorities = {"Permissions.Categories.View"})
    @Sql("/multiple-categories.sql")
    @Test
    public void getCategoriesPageStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/categories")
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
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
