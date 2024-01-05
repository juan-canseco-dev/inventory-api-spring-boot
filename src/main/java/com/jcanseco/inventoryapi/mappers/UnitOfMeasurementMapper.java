package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UnitOfMeasurementMapper {
    UnitOfMeasurementDto entityToDto(UnitOfMeasurement entity);
    @Mapping(target = "id", ignore = true)
    UnitOfMeasurement createDtoToEntity(CreateUnitOfMeasurementDto dto);

    default PagedList<UnitOfMeasurementDto> pageToPagedList(Page<UnitOfMeasurement> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

}
