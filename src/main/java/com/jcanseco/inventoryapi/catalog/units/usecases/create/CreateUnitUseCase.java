package com.jcanseco.inventoryapi.catalog.units.usecases.create;

import com.jcanseco.inventoryapi.catalog.units.dto.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.catalog.units.mapping.UnitOfMeasurementMapper;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUnitUseCase {

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final UnitOfMeasurementMapper unitOfMeasurementMapper;

    public Long execute(CreateUnitOfMeasurementDto dto) {
        var unit = unitOfMeasurementMapper.createDtoToEntity(dto);
        var newUnit = unitOfMeasurementRepository.saveAndFlush(unit);
        return newUnit.getId();
    }
}

