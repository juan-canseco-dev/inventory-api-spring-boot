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

    private Category createCategory(String name) {
        return Category.builder()
                .name(name)
                .build();
    }
    @BeforeEach
    public void setup() {
        var categories = List.of(
                createCategory("Electronics"),
                createCategory("Clothing"),
                createCategory("Home and Garden"),
                createCategory("Books"),
                createCategory("Sports and Outdoors"),
                createCategory("Health and Beauty"),
                createCategory("Toys and Games"),
                createCategory("Automotive"),
                createCategory("Furniture"),
                createCategory("Music")
        );
        this.repository.saveAllAndFlush(categories);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void getCategoriesWithEmptyNameShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCategoriesWithNamePresentShouldReturnList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","c");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCategoriesWhenOrderByIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("orderBy","invalid_field");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoriesWhenSortOrderIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortOrder","invalid_sort_order");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
