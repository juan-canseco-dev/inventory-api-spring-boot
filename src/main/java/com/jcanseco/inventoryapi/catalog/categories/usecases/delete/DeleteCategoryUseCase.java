package com.jcanseco.inventoryapi.catalog.categories.usecases.delete;

import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private static final String NOT_FOUND_CATEGORY_MESSAGE = "Category with the Id {%d} was not found.";

    private final CategoryRepository categoryRepository;

    public void execute(Long categoryId) {
        var category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_CATEGORY_MESSAGE, categoryId)));

        categoryRepository.delete(category);
    }
}

