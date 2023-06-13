package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Categories Repository Tests")
@SpringBootTest
public class CategoriesTests {

    @Autowired
    private CategoryRepository repository;

    @Test
    public void createCategory() {

        var category = Category.builder().name("Electronics").build();
        var newCategory = repository.saveAndFlush(category);

        assertTrue(newCategory.getId() > 0);
        assertEquals(category.getName(), newCategory.getName());

        var categoryOpt = repository.findById(newCategory.getId());
        assertTrue(categoryOpt.isPresent());

    }

}
