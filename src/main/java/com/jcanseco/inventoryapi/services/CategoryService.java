package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.CategoryDto;
import com.jcanseco.inventoryapi.dtos.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.UpdateCategoryDto;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryDto dto);
    CategoryDto updateCategory(UpdateCategoryDto dto);
    void deleteCategory(Long categoryId);
    CategoryDto getCategoryById(CategoryDto dto);
    List<CategoryDto> getCategories(String name);
    Page<CategoryDto> getCategoriesPaged(GetCategoriesRequest request);
}
