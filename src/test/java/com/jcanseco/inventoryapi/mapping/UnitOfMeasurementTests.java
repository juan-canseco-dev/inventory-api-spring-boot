package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.mappers.UnitOfMeasurementMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

public class UnitOfMeasurementTests {
    private final UnitOfMeasurementMapper mapper = Mappers.getMapper(UnitOfMeasurementMapper.class);
    @Test
    public void entityToDto() {
        assertNotNull(mapper);
        var entity = new UnitOfMeasurement(1L, "Box");
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
}