package com.jcanseco.inventoryapi.integration.categories;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetCategoryTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository repository;
    private Category savedCategory;

    @BeforeEach
    public void setup() {
        savedCategory = repository.saveAndFlush( Category.builder().name("Electronics").build());
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void getCategoryByIdWhenCategoryExistsStatusShouldBeOk() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/categories/" + savedCategory.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value(savedCategory.getName()));
    }

    @Test
    public void getCategoryByIdWhenCategoryNotExistsStatusShouldBeNotFound() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/categories/1000")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}

