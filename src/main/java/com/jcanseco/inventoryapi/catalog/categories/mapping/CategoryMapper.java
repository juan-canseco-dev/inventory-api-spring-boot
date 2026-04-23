package com.jcanseco.inventoryapi.catalog.categories.mapping;

import com.jcanseco.inventoryapi.catalog.categories.dto.CategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.dto.CreateCategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;





@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto entityToDto(Category category);
    @Mapping(target = "id", ignore = true)
    Category createDtoToEntity(CreateCategoryDto dto);

    default PagedList<CategoryDto> pageToPagedList(Page<Category> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}






