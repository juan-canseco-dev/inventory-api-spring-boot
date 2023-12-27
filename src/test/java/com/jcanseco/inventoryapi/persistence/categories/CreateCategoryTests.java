package com.jcanseco.inventoryapi.persistence.categories;

import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newCategory;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Create Category Persistence Tests")
@SpringBootTest
public class CreateCategoryTests {

    @Autowired
    private CategoryRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createCategoryShouldGenerateId() {

        var category = newCategory("New Category");
        var newCategory = repository.saveAndFlush(category);

        assertTrue(newCategory.getId() > 0);
        assertEquals(category.getName(), newCategory.getName());

        var createdCategory = repository.findById(newCategory.getId());
        assertTrue(createdCategory.isPresent());
    }
}
