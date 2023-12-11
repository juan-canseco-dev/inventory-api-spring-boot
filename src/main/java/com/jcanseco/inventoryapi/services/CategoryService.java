package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryDto dto);
    CategoryDto updateCategory(UpdateCategoryDto dto);
    void deleteCategory(Long categoryId);
    CategoryDto getCategoryById(Long categoryId);
    List<CategoryDto> getCategories(String name);
    PagedList<CategoryDto> getCategoriesPaged(GetCategoriesRequest request);
}
