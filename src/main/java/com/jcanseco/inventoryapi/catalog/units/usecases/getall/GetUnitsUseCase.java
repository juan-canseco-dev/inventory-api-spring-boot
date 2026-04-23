package com.jcanseco.inventoryapi.catalog.units.usecases.getall;

import com.jcanseco.inventoryapi.catalog.units.dto.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.catalog.units.dto.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.catalog.units.domain.UnitOfMeasurement;
import com.jcanseco.inventoryapi.catalog.units.mapping.UnitOfMeasurementMapper;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementSpecifications;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.shared.utils.IndexUtility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GetUnitsUseCase {

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final UnitOfMeasurementMapper unitOfMeasurementMapper;
    private final IndexUtility indexUtility;

    public List<UnitOfMeasurementDto> execute(GetUnitsOfMeasurementRequest request) {
        var specification = composeSpecification(request);
        return unitOfMeasurementRepository.findAll(specification)
                .stream()
                .map(unitOfMeasurementMapper::entityToDto)
                .toList();
    }

    public PagedList<UnitOfMeasurementDto> executePaged(GetUnitsOfMeasurementRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = unitOfMeasurementRepository.findAll(specification, pageRequest);
        return unitOfMeasurementMapper.pageToPagedList(page);
    }

    private Specification<UnitOfMeasurement> composeSpecification(GetUnitsOfMeasurementRequest request) {
        Specification<UnitOfMeasurement> specification = Specification.where(null);

        if (StringUtils.hasText(request.getName())) {
            specification = specification.and(UnitOfMeasurementSpecifications.byNameLike(request.getName()));
        }

        var orderByField = !StringUtils.hasText(request.getOrderBy()) ? "id" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return UnitOfMeasurementSpecifications.orderBy(specification, orderByField, isAscending);
    }
}

