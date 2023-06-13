package com.jcanseco.inventoryapi.services.impl;

import com.jcanseco.inventoryapi.dtos.CategoryDto;
import com.jcanseco.inventoryapi.dtos.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.UpdateCategoryDto;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto createCategory(CreateCategoryDto dto) {
        return null;
    }

    @Override
    public CategoryDto updateCategory(UpdateCategoryDto dto) {
        return null;
    }

    @Override
    public void deleteCategory(Long categoryId) {

    }

    @Override
    public CategoryDto getCategoryById(CategoryDto dto) {
        return null;
    }

    @Override
    public List<CategoryDto> getCategories(String name) {
        return repository.findAllByNameContainingOrderByName(name)
                .stream()
                .map(mapper::entityToDto).
                toList();
    }

    @Override
    public Page<CategoryDto> getCategoriesPaged(GetCategoriesRequest request) {
        return null;
    }

}
