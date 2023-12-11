package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import java.util.List;

public interface UnitService {
    UnitOfMeasurementDto getUnitById(Long unitId);
    UnitOfMeasurementDto createUnit(CreateUnitOfMeasurementDto dto);
    UnitOfMeasurementDto updateUnit(UpdateUnitOfMeasurementDto dto);
    void deleteUnit(Long unitId);
    List<UnitOfMeasurementDto> getUnits(String name);
    PagedList<UnitOfMeasurementDto> getUnitsPage(GetUnitsOfMeasurementRequest request);
}
