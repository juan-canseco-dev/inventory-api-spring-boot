package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.mappers.UnitOfMeasurementMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.newUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UnitOfMeasurementMapperTests {
    private final UnitOfMeasurementMapper mapper = Mappers.getMapper(UnitOfMeasurementMapper.class);
    @Test
    public void entityToDto() {

        assertNotNull(mapper);

        var entity = newUnit(1L, "Piece");
        var dto = mapper.entityToDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    public void createDtoToEntity() {
        assertNotNull(mapper);
        var dto = new CreateUnitOfMeasurementDto("Box");
        var entity = mapper.createDtoToEntity(dto);
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var units = List.of(
                newUnit(1L, "Each"),
                newUnit(2L, "Piece")
        );

        var unitsDto = units.stream().map(mapper::entityToDto).toList();

        Page<UnitOfMeasurement> page = new PageImpl<>(units, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(unitsDto);
    }

}
