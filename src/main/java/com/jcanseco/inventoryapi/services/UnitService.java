package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import java.util.List;

public interface UnitService {
    UnitOfMeasurementDto getUnitById(Long unitId);
    Long createUnit(CreateUnitOfMeasurementDto dto);
    void updateUnit(UpdateUnitOfMeasurementDto dto);
    void deleteUnit(Long unitId);
    List<UnitOfMeasurementDto> getUnits(GetUnitsOfMeasurementRequest request);
    PagedList<UnitOfMeasurementDto> getUnitsPage(GetUnitsOfMeasurementRequest request);
}
