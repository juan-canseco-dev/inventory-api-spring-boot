package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.CategoryDto;
import com.jcanseco.inventoryapi.dtos.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryDto dto);
    CategoryDto updateCategory(UpdateCategoryDto dto);
    void deleteCategory(Long categoryId);
    CategoryDto getCategoryById(CategoryDto dto);
    List<CategoryDto> getCategories();
}
