package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.UnitOfMeasurementMapper;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.specifications.UnitOfMeasurementSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {

    private static final String NOT_FOUND_MESSAGE = "UnitOfMeasurement with the Id {%d} was not found.";

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final UnitOfMeasurementMapper unitOfMeasurementMapper;
    private final IndexUtility indexUtility;

    public Long createUnit(CreateUnitOfMeasurementDto dto) {
        var unit = unitOfMeasurementMapper.createDtoToEntity(dto);
        var newUnit = unitOfMeasurementRepository.saveAndFlush(unit);
        return newUnit.getId();
    }

    public void updateUnit(UpdateUnitOfMeasurementDto dto) {
        var unit = unitOfMeasurementRepository
                .findById(dto.getUnitOfMeasurementId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, dto.getUnitOfMeasurementId())));

        unit.setName(dto.getName());

        unitOfMeasurementRepository.saveAndFlush(unit);
    }

    public void deleteUnit(Long unitId) {
        var unit = unitOfMeasurementRepository
                .findById(unitId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, unitId)));

        unitOfMeasurementRepository.delete(unit);
    }

    public UnitOfMeasurementDto getUnitById(Long unitId) {
        return unitOfMeasurementRepository
                .findById(unitId)
                .map(unitOfMeasurementMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, unitId)));
    }

    private Specification<UnitOfMeasurement> composeSpecification(GetUnitsOfMeasurementRequest request) {

        Specification<UnitOfMeasurement> specification = Specification.where(null);

        if (StringUtils.hasText(request.getName())) {
            specification = specification.and(UnitOfMeasurementSpecifications.byNameLike(request.getName()));
        }

        var orderByField = !StringUtils.hasText(request.getOrderBy())? "id" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return UnitOfMeasurementSpecifications.orderBy(
                specification,
                orderByField,
                isAscending
        );
    }

    public List<UnitOfMeasurementDto> getUnits(GetUnitsOfMeasurementRequest request) {
        var specification = composeSpecification(request);
        return unitOfMeasurementRepository.findAll(specification)
                .stream()
                .map(unitOfMeasurementMapper::entityToDto)
                .toList();
    }

    public PagedList<UnitOfMeasurementDto> getUnitsPage(GetUnitsOfMeasurementRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = unitOfMeasurementRepository.findAll(specification, pageRequest);
        return unitOfMeasurementMapper.pageToPagedList(page);
    }
}
