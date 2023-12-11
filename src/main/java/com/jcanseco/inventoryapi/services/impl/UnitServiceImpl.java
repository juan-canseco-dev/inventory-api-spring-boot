package com.jcanseco.inventoryapi.services.impl;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.UnitOfMeasurementMapper;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.services.UnitService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UnitServiceImpl implements UnitService {

    private final UnitOfMeasurementRepository repository;
    private final UnitOfMeasurementMapper mapper;

    @Override
    public UnitOfMeasurementDto createUnit(CreateUnitOfMeasurementDto dto) {
        var unit = mapper.createDtoToEntity(dto);
        var newUnit = repository.saveAndFlush(unit);
        return mapper.entityToDto(newUnit);
    }

    @Override
    public UnitOfMeasurementDto updateUnit(UpdateUnitOfMeasurementDto dto) {
        var unit = repository
                .findById(dto.getUnitOfMeasurementId())
                .orElseThrow(() -> new NotFoundException(String.format("UnitOfMeasurement with the Id {%d} was not found.", dto.getUnitOfMeasurementId())));

        unit.setName(dto.getName());

        var updatedUnit = repository.saveAndFlush(unit);
        return mapper.entityToDto(updatedUnit);
    }

    @Override
    public void deleteUnit(Long unitId) {
        var unit = repository
                .findById(unitId)
                .orElseThrow(() -> new NotFoundException(String.format("UnitOfMeasurement with the Id {%d} was not found.", unitId)));

        repository.delete(unit);
    }

    @Override
    public UnitOfMeasurementDto getUnitById(Long unitId) {
        return repository
                .findById(unitId)
                .map(mapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format("UnitOfMeasurement with the Id {%d} was not found.", unitId)));
    }

    @Override
    public List<UnitOfMeasurementDto> getUnits(String name) {
        var filterName = name == null? "" : name;
        return repository.findAllByNameContainingOrderByName(filterName)
                .stream()
                .map(mapper::entityToDto).
                toList();
    }

    @Override
    public PagedList<UnitOfMeasurementDto> getUnitsPage(GetUnitsOfMeasurementRequest request) {

        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();
        var filterName = request.getName() == null? "" : request.getName();
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = repository.findAllByNameContainingOrderByName(filterName, pageRequest);

        var items = page.get().map(mapper::entityToDto).toList();
        var totalPages = page.getTotalPages();
        var totalElements = page.getTotalElements();

        return new PagedList<>(
                items,
                request.getPageNumber(),
                pageSize,
                totalPages,
                totalElements
        );
    }
}
