package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.CategoryDto;
import com.jcanseco.inventoryapi.dtos.CreateCategoryDto;
import com.jcanseco.inventoryapi.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto entityToDto(Category category);
    @Mapping(target = "id", ignore = true)
    Category createDtoToEntity(CreateCategoryDto dto);
}
