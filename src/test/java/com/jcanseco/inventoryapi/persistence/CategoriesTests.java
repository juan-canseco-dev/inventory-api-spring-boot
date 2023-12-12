package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Categories Repository Tests")
@SpringBootTest
public class CategoriesTests {

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
    public void createCategoryWhenValidCategoryReturnSavedCategoryWithGeneratedId() {

        var category = createCategory("New category");
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

    @Test
    public void findAllPagedCategoriesByNameWhenInputIsValidShouldReturnValidPage() {
        var request = PageRequest.of(0, 3);
        var page = repository.findAllByNameContaining("e", request);

        assertNotNull(page.getContent());
        assertEquals(3, page.getContent().size());
        assertEquals(2, page.getTotalPages());
        assertEquals(6, page.getTotalElements());
    }

    @Test
    public void findAllPagedCategoriesWhenNameIsEmptyShouldReturnValidPageWithAllItems() {
        var request = PageRequest.of(0, 10);
        var page = repository.findAllByNameContaining("", request);

        assertNotNull(page.getContent());
        assertEquals(10, page.getContent().size());
    }
}
