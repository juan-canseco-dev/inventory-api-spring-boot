package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
public class CategoryServiceUnitTests {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void createCategoryCreateShouldBeSuccessful() {

        var createdCategoryId = 1L;

        var dto = CreateCategoryDto.builder()
                .name("New Category")
                .build();

        var mappedCategory = Category.builder()
                        .name("New Category")
                        .build();

        var newCategory = Category.builder()
                .id(createdCategoryId)
                .name("New Category")
                .build();

        when(categoryMapper.createDtoToEntity(dto)).thenReturn(mappedCategory);
        when(categoryRepository.saveAndFlush(mappedCategory)).thenReturn(newCategory);

        var resultCategoryId = categoryService.createCategory(dto);
        assertEquals(createdCategoryId, resultCategoryId);
    }

    @Test
    public void updateCategoryWhenCategoryExistsUpdateShouldBeSuccessful() {
        var categoryId = 1L;

        var dto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name("Updated Category Name")
                .build();

        var foundCategory = Category.builder()
                        .id(categoryId)
                        .name("New Category")
                        .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(foundCategory));
        categoryService.updateCategory(dto);

        verify(categoryRepository, times(1)).saveAndFlush(foundCategory);

        var categoryArgCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).saveAndFlush(categoryArgCaptor.capture());
        var updatedCategory = categoryArgCaptor.getValue();
        assertNotNull(updatedCategory);
        assertEquals(categoryId, updatedCategory.getId());
        assertEquals(dto.getName(), updatedCategory.getName());
    }

    @Test
    public void updateCategoryWhenCategoryDoNotExistsShouldThrowNotFoundException() {
        var categoryId = 1L;

        var dto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name("Updated Category Name")
                .build();

        when(categoryRepository.findById(categoryId)).thenThrow(new NotFoundException("Category Not Found"));
        assertThrows(NotFoundException.class, () -> categoryService.updateCategory(dto));
    }

    @Test
    public void deleteCategoryWhenCategoryExistsDeleteShouldBeSuccessful() {

        var categoryId = 1L;
        var foundCategory = Category
                .builder()
                .id(categoryId)
                .name("Found Category")
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(foundCategory));
        doNothing().when(categoryRepository).delete(foundCategory);
        categoryService.deleteCategory(categoryId);
        verify(categoryRepository, times(1)).delete(foundCategory);
    }

    @Test
    public void deleteCategoryWhenCategoryDoNotExistsShouldThrowNotFoundException() {
        var categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenThrow(new NotFoundException("Category Not Found"));
        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(categoryId));
    }

    @Test
    public void getCategoryWhenCategoryExistsGetShouldBeSuccessful() {

        var categoryId = 1L;

        var foundCategory = Category.builder()
                .id(categoryId)
                .name("Found Category")
                .build();

        var categoryDto = CategoryDto.builder()
                .id(categoryId)
                .name("Found Category")
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(foundCategory));
        when(categoryMapper.entityToDto(foundCategory)).thenReturn(categoryDto);

        var resultDto = categoryService.getCategoryById(categoryId);
        assertEquals(categoryDto, resultDto);
    }

    @Test
    public void getCategoryWhenCategoryDoNotExistsShouldThrowNotFoundException() {
        var categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenThrow(new NotFoundException("Category Not Found"));
        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }
}
