package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.services.CategoryService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newCategory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceUnitTests {
    @Mock
    private CategoryRepository categoryRepository;
    @Spy
    private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
    @Spy
    private IndexUtility indexUtility = new IndexUtility();
    @InjectMocks
    private CategoryService categoryService;
    private List<Category> categories;
    @BeforeEach
    public void setup() {
        categories = List.of(
                newCategory(1L, "Electronics"),
                newCategory(2L, "Home & Garden")
        );
    }

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

    @Test
    public void getCategoriesShouldReturnList() {

        var expectedResult = categories.stream()
                .map(categoryMapper::entityToDto)
                .toList();

        var request = GetCategoriesRequest.builder().build();
        Specification<Category> mockSpec = any();
        when(categoryRepository.findAll(mockSpec)).thenReturn(categories);
        var result = categoryService.getCategories(request);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertThat(result).hasSameElementsAs(expectedResult);
    }

    @Test
    public void getCategoriesPageShouldReturnPagedList() {

        var totalCategoriesInDb = 4;

        var totalPages = 2;

        var expectedItems = categories.stream()
                .map(categoryMapper::entityToDto)
                .toList();


        var request = GetCategoriesRequest.builder()
                .pageNumber(1)
                .pageSize(2)
                .build();

        Specification<Category> mockSpec = any();
        PageRequest mockPageRequest = any();
        Page<Category> mockPage = new PageImpl<>(
                categories,
                Pageable.ofSize(2),
                totalCategoriesInDb
        );

        when(categoryRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = categoryService.getCategoriesPaged(request);
        assertNotNull(pagedList);
        assertEquals(request.getPageNumber(), pagedList.getPageNumber());
        assertEquals(request.getPageSize(), pagedList.getPageSize());
        assertEquals(totalCategoriesInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(expectedItems);
    }
}
