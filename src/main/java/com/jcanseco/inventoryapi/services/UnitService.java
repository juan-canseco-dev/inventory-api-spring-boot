package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import org.springframework.data.domain.Page;
import java.util.List;

public interface UnitService {
    UnitOfMeasurementDto getUnitById(Long unitId);
    UnitOfMeasurementDto createUnit(CreateUnitOfMeasurementDto dto);
    UnitOfMeasurementDto updateUnit(UpdateUnitOfMeasurementDto dto);
    void deleteUnit(Long unitId);
    List<UnitOfMeasurementDto> getUnits(String name);
    Page<UnitOfMeasurementDto> getUnitsPage(GetUnitsOfMeasurementRequest request);
}
