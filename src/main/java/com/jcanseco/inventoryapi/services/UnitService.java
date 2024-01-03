package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.UnitOfMeasurementMapper;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final UnitOfMeasurementMapper unitOfMeasurementMapper;

    public Long createUnit(CreateUnitOfMeasurementDto dto) {
        var unit = unitOfMeasurementMapper.createDtoToEntity(dto);
        var newUnit = unitOfMeasurementRepository.saveAndFlush(unit);
        return newUnit.getId();
    }

    public void updateUnit(UpdateUnitOfMeasurementDto dto) {
        var unit = unitOfMeasurementRepository
                .findById(dto.getUnitOfMeasurementId())
                .orElseThrow(() -> new NotFoundException(String.format("UnitOfMeasurement with the Id {%d} was not found.", dto.getUnitOfMeasurementId())));

        unit.setName(dto.getName());

        unitOfMeasurementRepository.saveAndFlush(unit);
    }

    public void deleteUnit(Long unitId) {
        var unit = unitOfMeasurementRepository
                .findById(unitId)
                .orElseThrow(() -> new NotFoundException(String.format("UnitOfMeasurement with the Id {%d} was not found.", unitId)));

        unitOfMeasurementRepository.delete(unit);
    }

    public UnitOfMeasurementDto getUnitById(Long unitId) {
        return unitOfMeasurementRepository
                .findById(unitId)
                .map(unitOfMeasurementMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format("UnitOfMeasurement with the Id {%d} was not found.", unitId)));
    }

    private boolean sortOrderIsAscending(GetUnitsOfMeasurementRequest request) {
        if (request.getSortOrder() == null) {
            return true;
        }
        return request.getSortOrder().equals("asc") || request.getSortOrder().equals("ascending");
    }


    private String getOrderBy(GetUnitsOfMeasurementRequest request) {
        if (request.getOrderBy() == null) {
            return "name";
        }
        return request.getOrderBy();
    }

    private Sort getSortOrder(GetUnitsOfMeasurementRequest request) {
        var ascending = sortOrderIsAscending(request);
        var orderBy = getOrderBy(request);
        if (ascending) {
            return Sort.by(orderBy).ascending();
        }
        return Sort.by(orderBy).descending();
    }

    public List<UnitOfMeasurementDto> getUnits(GetUnitsOfMeasurementRequest request) {
        var filterName = request.getName() == null? "" : request.getName();
        var sort = getSortOrder(request);
        return unitOfMeasurementRepository.findAllByNameContaining(filterName, sort)
                .stream()
                .map(unitOfMeasurementMapper::entityToDto).
                toList();
    }

    public PagedList<UnitOfMeasurementDto> getUnitsPage(GetUnitsOfMeasurementRequest request) {

        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();
        var filterName = request.getName() == null? "" : request.getName();
        var sort = getSortOrder(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        var page = unitOfMeasurementRepository.findAllByNameContaining(filterName, pageRequest);

        var items = page.get().map(unitOfMeasurementMapper::entityToDto).toList();
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
