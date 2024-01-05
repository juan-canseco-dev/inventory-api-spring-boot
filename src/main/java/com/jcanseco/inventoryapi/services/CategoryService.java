package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private static final String NOT_FOUND_CATEGORY_MESSAGE = "Category with the Id {%d} was not found.";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final IndexUtility indexUtility;

    public Long createCategory(CreateCategoryDto dto) {
        var category = categoryMapper.createDtoToEntity(dto);
        var newCategory = categoryRepository.saveAndFlush(category);
        return newCategory.getId();
    }

    public void updateCategory(UpdateCategoryDto dto) {

        var category = categoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_CATEGORY_MESSAGE, dto.getCategoryId())));

        category.setName(dto.getName());

        categoryRepository.saveAndFlush(category);
    }

    public void deleteCategory(Long categoryId) {
        var category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_CATEGORY_MESSAGE, categoryId)));

        categoryRepository.delete(category);
    }


    public CategoryDto getCategoryById(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .map(categoryMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_CATEGORY_MESSAGE, categoryId)));
    }


    public List<CategoryDto> getCategories(GetCategoriesRequest request) {

        var isAscendingOrder = indexUtility.isAscendingOrder(request.getSortOrder());

        var specification = CategoryRepository.Specs.composeSpecification(
                request.getName(),
                request.getOrderBy(),
                isAscendingOrder
        );

        return categoryRepository.findAll(specification)
                .stream()
                .map(categoryMapper::entityToDto).
                toList();
    }

    public PagedList<CategoryDto> getCategoriesPaged(GetCategoriesRequest request) {

        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var isAscendingOrder = indexUtility.isAscendingOrder(request.getSortOrder());

        var specification = CategoryRepository.Specs.composeSpecification(
                request.getName(),
                request.getOrderBy(),
                isAscendingOrder
        );

        var pageRequest = PageRequest.of(pageNumber, pageSize);

        var page = categoryRepository.findAll(specification, pageRequest);

        var items = page.get().map(categoryMapper::entityToDto).toList();
        var totalPages = page.getTotalPages();
        var totalElements = page.getTotalElements();

        return new PagedList<>(
                items,
                request.getPageNumber(),
                pageSize,
                totalPages,
                totalElements
        );
    }
}
