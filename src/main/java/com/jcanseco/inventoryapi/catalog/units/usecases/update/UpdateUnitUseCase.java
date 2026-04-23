package com.jcanseco.inventoryapi.catalog.units.usecases.update;

import com.jcanseco.inventoryapi.catalog.units.dto.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUnitUseCase {

    private static final String NOT_FOUND_MESSAGE = "UnitOfMeasurement with the Id {%d} was not found.";

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;

    public void execute(UpdateUnitOfMeasurementDto dto) {
        var unit = unitOfMeasurementRepository
                .findById(dto.getUnitOfMeasurementId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, dto.getUnitOfMeasurementId())));

        unit.setName(dto.getName());
        unitOfMeasurementRepository.saveAndFlush(unit);
    }
}

