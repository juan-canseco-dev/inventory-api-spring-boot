package com.jcanseco.inventoryapi.catalog.units.usecases.delete;

import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUnitUseCase {

    private static final String NOT_FOUND_MESSAGE = "UnitOfMeasurement with the Id {%d} was not found.";

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;

    public void execute(Long unitId) {
        var unit = unitOfMeasurementRepository
                .findById(unitId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, unitId)));

        unitOfMeasurementRepository.delete(unit);
    }
}

