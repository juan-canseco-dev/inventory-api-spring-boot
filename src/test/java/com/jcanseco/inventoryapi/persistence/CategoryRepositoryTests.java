package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newCategory;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository repository;

    @Test
    public void createCategoryShouldGenerateId() {

        var category = newCategory("New Category");
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
        var specification = CategoryRepository.Specs.orderBy(
                CategoryRepository.Specs.byNameLike("au"),
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

    @Test
    @Sql("/multiple-categories.sql")
    public void findCategoriesByComposeSpecificationWhenNameAndOrderByAreNotPresentShouldReturnList() {
        var firstCategoryId = 1L;
        var spec = CategoryRepository.Specs.composeSpecification(
                "",
                "",
                true
        );
        var foundCategories = repository.findAll(spec);
        assertNotNull(foundCategories);
        assertEquals(foundCategories.size(), 10);
        var firstCategory = foundCategories.get(0);
        assertEquals(firstCategoryId, firstCategory.getId());
    }



    @Test
    @Sql("/multiple-categories.sql")
    public void composeSpecificationWhenNameIsNullOrEmptyShouldReturnList() {
        var firstCategoryId = 10L;
        var spec = CategoryRepository.Specs.composeSpecification(
                "",
                "id",
                false
        );
        var foundCategories = repository.findAll(spec);
        assertNotNull(foundCategories);
        assertEquals(10, foundCategories.size());
        var firstCategory = foundCategories.get(0);
        assertEquals(firstCategoryId, firstCategory.getId());
    }

    @Test
    @Sql("/multiple-categories.sql")
    public void composeSpecificationWhenNameIsPresentShouldReturnExpectedList() {
        var spec = CategoryRepository.Specs.composeSpecification(
                "a",
                "name",
                true
        );
        var foundCategories = repository.findAll(spec);
        assertNotNull(foundCategories);
        assertEquals(5, foundCategories.size());
    }


}
