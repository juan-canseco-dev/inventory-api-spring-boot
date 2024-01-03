package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Long createCategory(CreateCategoryDto dto) {
        var category = categoryMapper.createDtoToEntity(dto);
        var newCategory = categoryRepository.saveAndFlush(category);
        return newCategory.getId();
    }

    public void updateCategory(UpdateCategoryDto dto) {

        var category = categoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(String.format("Category with the Id {%d} was not found.", dto.getCategoryId())));

        category.setName(dto.getName());

        categoryRepository.saveAndFlush(category);
    }

    public void deleteCategory(Long categoryId) {
        var category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with the Id {%d} was not found.", categoryId)));

        categoryRepository.delete(category);
    }


    public CategoryDto getCategoryById(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .map(categoryMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format("Category with the Id {%d} was not found.", categoryId)));
    }

    private boolean sortOrderIsAscending(GetCategoriesRequest request) {
        if (request.getSortOrder() == null) {
            return true;
        }
        return request.getSortOrder().equals("asc") || request.getSortOrder().equals("ascending");
    }


    private String getOrderBy(GetCategoriesRequest request) {
        if (request.getOrderBy() == null) {
            return "name";
        }
        return request.getOrderBy();
    }

    private Sort getSortOrder(GetCategoriesRequest request) {
        var ascending = sortOrderIsAscending(request);
        var orderBy = getOrderBy(request);
        if (ascending) {
            return Sort.by(orderBy).ascending();
        }
        return Sort.by(orderBy).descending();
    }

    public List<CategoryDto> getCategories(GetCategoriesRequest request) {

        var filterName = request.getName() == null? "" : request.getName();
        var sort = getSortOrder(request);
        return categoryRepository.findAllByNameContaining(filterName, sort)
                .stream()
                .map(categoryMapper::entityToDto).
                toList();
    }

    public PagedList<CategoryDto> getCategoriesPaged(GetCategoriesRequest request) {
        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();
        var filterName = request.getName() == null? "" : request.getName();

        var sort = getSortOrder(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        var page = categoryRepository.findAllByNameContaining(filterName, pageRequest);

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
