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
public class GetCategoriesPageTests {

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
    public void getCategoriesPageShouldReturnPagedList() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "3")
                .param("name", "e");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(6))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }

    @Test
    public void getCategoriesPageWhenPageNumberOrPageSizeAreNegativeStatusShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "-1")
                .param("pageSize", "1");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoriesPageWhenOrderByIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("orderBy","invalid_order_by");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoriesWhenSortOrderIsInvalidShouldBeBadRequest() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/units/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .param("sortOrder","invalid_sort_order");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

}