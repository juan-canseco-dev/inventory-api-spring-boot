package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.specifications.CategorySpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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


    private Specification<Category> composeSpecification(GetCategoriesRequest request) {

        Specification<Category> specification = Specification.where(null);

        if (StringUtils.hasText(request.getName())) {
            specification = specification.and(CategorySpecifications.byNameLike(request.getName()));
        }

        var orderByField = !StringUtils.hasText(request.getOrderBy())? "id" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return CategorySpecifications.orderBy(
                specification,
                orderByField,
                isAscending
        );
    }

    public List<CategoryDto> getCategories(GetCategoriesRequest request) {

        var specification = composeSpecification(request);

        return categoryRepository.findAll(specification)
                .stream()
                .map(categoryMapper::entityToDto).
                toList();
    }

    public PagedList<CategoryDto> getCategoriesPaged(GetCategoriesRequest request) {

        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var specification = composeSpecification(request);

        var pageRequest = PageRequest.of(pageNumber, pageSize);

        var page = categoryRepository.findAll(specification, pageRequest);

        return categoryMapper.pageToPagedList(page);
    }
}
