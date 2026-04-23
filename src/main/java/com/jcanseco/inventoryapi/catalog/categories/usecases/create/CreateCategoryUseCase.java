package com.jcanseco.inventoryapi.catalog.categories.usecases.create;

import com.jcanseco.inventoryapi.catalog.categories.dto.CreateCategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.mapping.CategoryMapper;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Long execute(CreateCategoryDto dto) {
        var category = categoryMapper.createDtoToEntity(dto);
        var newCategory = categoryRepository.saveAndFlush(category);
        return newCategory.getId();
    }
}

