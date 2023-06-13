package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Categories Repository Tests")
@SpringBootTest
public class CategoriesTests {

    @Autowired
    private CategoryRepository repository;
    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createCategoryWhenValidCategoryReturnSavedCategoryWithGeneratedId() {

        var category = Category.builder().name("Electronics").build();
        var newCategory = repository.saveAndFlush(category);

        assertTrue(newCategory.getId() > 0);
        assertEquals(category.getName(), newCategory.getName());

        var categoryOpt = repository.findById(newCategory.getId());
        assertTrue(categoryOpt.isPresent());

    }

    @Test
    public void findAllCategoriesByNameWhenNameIsEmptyShouldReturnList() {

        var categories = List.of(
                Category.builder().name("Electronics").build(),
                Category.builder().name("Video Games").build()
        );

        var savedCategories = this.repository.saveAllAndFlush(categories);
        var foundCategories = repository.findAllByNameContainingOrderByName("");
        assertTrue(
                savedCategories.size() == foundCategories.size() &&
                        savedCategories.containsAll(foundCategories) &&
                        foundCategories.containsAll(savedCategories)
        );
    }

    @Test
    public void findAllCategoriesByNameContainingWhenContains() {
        var categories = List.of(
                Category.builder().name("Electronics").build(),
                Category.builder().name("Video Games").build()
        );
        this.repository.saveAllAndFlush(categories);
        var foundCategories = repository.findAllByNameContainingOrderByName("video");
        assertEquals(1, foundCategories.size());
    }

    @Test
    public void findAllPagedCategoriesByNameWhenInputIsValidShouldReturnValidPage() {

        var categories = List.of(
                Category.builder().name("Electronics").build(),
                Category.builder().name("Video").build(),
                Category.builder().name("Video Games").build()
        );

        repository.saveAllAndFlush(categories);

        var request = PageRequest.of(0, 10);
        var page = repository.findAllByNameContainingOrderByName("vi", request);

        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size());
    }

    @Test
    public void findAllPagedCategoriesWhenNameIsEmptyShouldReturnValidPageWithAllItems() {
        var categories = List.of(
                Category.builder().name("Electronics").build(),
                Category.builder().name("Video").build(),
                Category.builder().name("Video Games").build()
        );

        repository.saveAllAndFlush(categories);

        var request = PageRequest.of(0, 10);
        var page = repository.findAllByNameContainingOrderByName("", request);

        assertNotNull(page.getContent());
        assertEquals(3, page.getContent().size());
    }

}
