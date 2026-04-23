package com.jcanseco.inventoryapi.catalog.categories;

import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategorySpecifications;
import com.jcanseco.inventoryapi.shared.testing.TestcontainersConfiguration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repository;

    @Test
    void createCategoryShouldGenerateId() {
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
    void findCategoriesByNameLikeSpecificationShouldReturnList() {
        var expectedCategoryName = "Automotive";
        var specification = CategorySpecifications.orderBy(
                CategorySpecifications.byNameLike("au"),
                "name",
                true
        );

        var foundCategories = repository.findAll(specification);

        assertNotNull(foundCategories);
        assertFalse(foundCategories.isEmpty());
        assertEquals(expectedCategoryName, foundCategories.get(0).getName());
        assertEquals(2, foundCategories.size());
    }
}






