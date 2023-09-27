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
import java.util.List;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class GetCategoriesTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository repository;

    @BeforeEach
    public void setup() {
        var categories = List.of(
                Category.builder().name("Electronics").build(),
                Category.builder().name("Video").build(),
                Category.builder().name("Video Games").build()
        );
        repository.saveAllAndFlush(categories);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void getAllCategoriesWhenPageNumberOrPageSizeAreNullShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "v");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getAllCategoriesWhenPageNumberAndPageSizeArePresentShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "1")
                .param("name", "e");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.content", Matchers.notNullValue()))
                .andExpect(jsonPath("$.content.size()").value(1));
    }

    @Test
    public void getAllCategoriesWhenPageNumberOrPageSizeAreNegativeStatusShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "-1")
                .param("pageSize", "1");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}