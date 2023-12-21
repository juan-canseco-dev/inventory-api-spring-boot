package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import java.util.List;

public interface CategoryService {
    Long createCategory(CreateCategoryDto dto);
    void updateCategory(UpdateCategoryDto dto);
    void deleteCategory(Long categoryId);
    CategoryDto getCategoryById(Long categoryId);
    List<CategoryDto> getCategories(GetCategoriesRequest request);
    PagedList<CategoryDto> getCategoriesPaged(GetCategoriesRequest request);
}
