package com.jcanseco.inventoryapi.catalog.categories.usecases.update;

import com.jcanseco.inventoryapi.catalog.categories.dto.UpdateCategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCase {

    private static final String NOT_FOUND_CATEGORY_MESSAGE = "Category with the Id {%d} was not found.";

    private final CategoryRepository categoryRepository;

    public void execute(UpdateCategoryDto dto) {
        var category = categoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_CATEGORY_MESSAGE, dto.getCategoryId())));

        category.setName(dto.getName());
        categoryRepository.saveAndFlush(category);
    }
}

