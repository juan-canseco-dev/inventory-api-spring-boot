package com.jcanseco.inventoryapi.security.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerAuthorizationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @WithMockUser()
    @Test
    public void createCategoryWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
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
                .andExpect(status().isForbidden());
    }

    @Test
    public void createCategoryWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        mockMvc.perform(
                        post("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @WithMockUser()
    @Test
    public void updateCategoryWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
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
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateCategoryWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        mockMvc.perform(
                        put("/api/categories/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser()
    @Test
    public void deleteCategoryWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
        mockMvc.perform(
                        delete("/api/categories/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteCategoryWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        mockMvc.perform(
                        delete("/api/categories/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser()
    @Test
    public void getCategoryByIdWhenUserHasNotAuthorityStatusShouldBeForbidden() throws Exception {
        mockMvc.perform(
                        get("/api/categories/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getCategoryByIdWhenUserIsNotPresentStatusShouldBeUnauthorized() throws Exception {
        mockMvc.perform(
                        get("/api/categories/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser()
    @Test
    public void getCategoriesWhenUserHasNoAuthorityStatusShouldBeForbidden() throws Exception {
        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getCategoriesWhenUserIsNotPresentStatusShouldBeUnauthorized() throws  Exception {
        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
