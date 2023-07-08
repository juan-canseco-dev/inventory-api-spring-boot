package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnitOfMeasurementMapper {
    UnitOfMeasurementDto entityToDto(UnitOfMeasurement entity);
    @Mapping(target = "id", ignore = true)
    UnitOfMeasurement createDtoToEntity(CreateUnitOfMeasurementDto dto);
}
