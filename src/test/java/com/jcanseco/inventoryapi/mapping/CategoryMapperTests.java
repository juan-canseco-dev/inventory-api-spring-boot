package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryMapperTests {
    private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    public void entityToDto() {

        assertNotNull(mapper);

        var entity = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();
        var dto = mapper.entityToDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    public void createDtoToEntity() {
        assertNotNull(mapper);
        var dto = new CreateCategoryDto("Electronics");
        var entity = mapper.createDtoToEntity(dto);
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var categories = List.of(
                Category.builder()
                        .id(1L)
                        .name("Electronics")
                        .build(),
                Category.builder()
                        .id(2L)
                        .name("Home & Garden")
                        .build()
        );

        var categoriesDto = categories.stream().map(mapper::entityToDto).toList();

        Page<Category> page = new PageImpl<>(categories, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(categoriesDto);
    }
}
