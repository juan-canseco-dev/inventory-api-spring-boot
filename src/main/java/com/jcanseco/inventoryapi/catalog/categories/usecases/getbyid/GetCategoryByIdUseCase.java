package com.jcanseco.inventoryapi.catalog.categories.usecases.getbyid;

import com.jcanseco.inventoryapi.catalog.categories.dto.CategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.mapping.CategoryMapper;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCategoryByIdUseCase {

    private static final String NOT_FOUND_CATEGORY_MESSAGE = "Category with the Id {%d} was not found.";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto execute(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .map(categoryMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_CATEGORY_MESSAGE, categoryId)));
    }
}

