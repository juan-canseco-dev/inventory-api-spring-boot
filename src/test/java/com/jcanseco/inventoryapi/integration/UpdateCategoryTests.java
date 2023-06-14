package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.dtos.UpdateCategoryDto;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateCategoryTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CategoryRepository repository;

    private Category savedCategory;

    @BeforeEach
    public void setup() {
        savedCategory = repository.saveAndFlush(Category.builder().name("Electronics").build());
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void updateCategoryWhenCategoryExistsAndModelIsValidStatusShouldBeOk() throws Exception {

        var categoryId = savedCategory.getId();
        var updateCategory = new UpdateCategoryDto(categoryId, "Electronics & Games");


        var request = MockMvcRequestBuilders
                .put("/api/categories/" + categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCategory));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(updateCategory.getName()));
    }

    @Test
    public void updateCategoryWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var categoryId = savedCategory.getId();
        var updateCategory = new UpdateCategoryDto(categoryId, "");


        var request = MockMvcRequestBuilders
                .put("/api/categories/" + categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCategory));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCategoryWhenCategoryDoesNotExistsStatusShouldBeNotFound() throws Exception {

        var categoryId = 20000L;
        var updateCategory = new UpdateCategoryDto(categoryId, "Electronics & Games");


        var request = MockMvcRequestBuilders
                .put("/api/categories/" + categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCategory));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
