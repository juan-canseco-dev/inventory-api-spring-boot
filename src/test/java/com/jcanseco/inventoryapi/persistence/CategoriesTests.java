package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Categories Repository Tests")
@SpringBootTest
public class CategoriesTests {

    @Autowired
    private CategoryRepository repository;


    @BeforeEach
    public void setup() {
        var categories = List.of(
                newCategory("Electronics"),
                newCategory("Clothing"),
                newCategory("Home and Garden"),
                newCategory("Books"),
                newCategory("Sports and Outdoors"),
                newCategory("Health and Beauty"),
                newCategory("Toys and Games"),
                newCategory("Automotive"),
                newCategory("Furniture"),
                newCategory("Music")
        );
        this.repository.saveAllAndFlush(categories);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createCategoryWhenValidCategoryReturnSavedCategoryWithGeneratedId() {

        var category = newCategory("New category");
        var newCategory = repository.saveAndFlush(category);

        assertTrue(newCategory.getId() > 0);
        assertEquals(category.getName(), newCategory.getName());

        var categoryOpt = repository.findById(newCategory.getId());
        assertTrue(categoryOpt.isPresent());

    }

    @Test
    public void findAllCategoriesByNameWhenNameIsEmptyShouldReturnList() {
        var foundCategories = repository.findAllByNameContaining("", Sort.by("name").ascending());
        assertNotNull(foundCategories);
        assertEquals(10, foundCategories.size());
    }

    @Test
    public void findAllCategoriesByNameContainingWhenContains() {
        var foundCategories = repository.findAllByNameContaining("v", Sort.by("name").ascending());
        assertEquals(1, foundCategories.size());
    }
}
