package com.jcanseco.inventoryapi.catalog.units.usecases.getbyid;

import com.jcanseco.inventoryapi.catalog.units.dto.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.catalog.units.mapping.UnitOfMeasurementMapper;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUnitByIdUseCase {

    private static final String NOT_FOUND_MESSAGE = "UnitOfMeasurement with the Id {%d} was not found.";

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final UnitOfMeasurementMapper unitOfMeasurementMapper;

    public UnitOfMeasurementDto execute(Long unitId) {
        return unitOfMeasurementRepository
                .findById(unitId)
                .map(unitOfMeasurementMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, unitId)));
    }
}

