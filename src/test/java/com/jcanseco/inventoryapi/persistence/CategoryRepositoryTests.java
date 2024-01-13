package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.specifications.CategorySpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository repository;

    @Test
    public void createCategoryShouldGenerateId() {

        var category = Category.builder()
                .name("New Category")
                .build();

        var newCategory = repository.saveAndFlush(category);

        assertTrue(newCategory.getId() > 0);
        assertEquals(category.getName(), newCategory.getName());

        var createdCategory = repository.findById(newCategory.getId());
        assertTrue(createdCategory.isPresent());
    }

    @Test
    @Sql("/multiple-categories.sql")
    public void findCategoriesByNameLikeSpecificationShouldReturnList() {
        var expectedCategoryName = "Automotive";
        var specification = CategorySpecifications.orderBy(
                CategorySpecifications.byNameLike("au"),
                "name",
                true
        );
        var foundCategories = repository.findAll(specification);
        assertNotNull(foundCategories);
        assertFalse(foundCategories.isEmpty());
        var firstCategory = foundCategories.get(0);
        assertEquals(expectedCategoryName, firstCategory.getName());
        assertEquals(2, foundCategories.size());
    }
}
