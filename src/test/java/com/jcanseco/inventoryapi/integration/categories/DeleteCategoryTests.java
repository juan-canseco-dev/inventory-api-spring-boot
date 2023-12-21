package com.jcanseco.inventoryapi.integration.categories;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class DeleteCategoryTests {
    @Autowired
    private MockMvc mockMvc;

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
    public void deleteCategoryWhenCategoryExistsStatusShouldBeNoContent() throws Exception {

        var categoryId = savedCategory.getId();

        var request = MockMvcRequestBuilders
                .delete("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var categoryOpt = repository.findById(categoryId);
        assertTrue(categoryOpt.isEmpty());
    }


    @Test
    public void deleteCategoryWhenCategoryNotExistsStatusShouldBeNotFound() throws Exception {
        var categoryId = 5000L;

        var request = MockMvcRequestBuilders
                .delete("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

}
