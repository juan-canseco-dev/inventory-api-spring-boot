package com.jcanseco.inventoryapi.services.impl;

import com.jcanseco.inventoryapi.dtos.CategoryDto;
import com.jcanseco.inventoryapi.dtos.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.UpdateCategoryDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto createCategory(CreateCategoryDto dto) {
        var category = mapper.createDtoToEntity(dto);
        var newCategory = repository.saveAndFlush(category);
        return mapper.entityToDto(newCategory);
    }

    @Override
    public CategoryDto updateCategory(UpdateCategoryDto dto) {

        var category = repository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(String.format("Category with the Id {%d} was not found.", dto.getCategoryId())));

        category.setName(dto.getName());

        var updatedCategory = repository.saveAndFlush(category);
        return mapper.entityToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        var category = repository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with the Id {%d} was not found.", categoryId)));

        repository.delete(category);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return repository
                .findById(categoryId)
                .map(mapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format("Category with the Id {%d} was not found.", categoryId)));
    }

    @Override
    public List<CategoryDto> getCategories(String name) {
        var filterName = name == null? "" : name;
        return repository.findAllByNameContainingOrderByName(filterName)
                .stream()
                .map(mapper::entityToDto).
                toList();
    }

    @Override
    public Page<CategoryDto> getCategoriesPaged(GetCategoriesRequest request) {
        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();
        var filterName = request.getName() == null? "" : request.getName();
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = repository.findAllByNameContainingOrderByName(filterName, pageRequest);
        return page.map(mapper::entityToDto);
    }
}
