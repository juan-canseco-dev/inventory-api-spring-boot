package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.mappers.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryMapperTests {
    private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    public void entityToDto() {
        assertNotNull(mapper);

        var entity = Category.builder().id(1L).name("Electronics").build();
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

}
