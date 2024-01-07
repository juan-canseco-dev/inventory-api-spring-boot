package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.*;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.SupplierMapper;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.specifications.SupplierSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private static final String NOT_FOUND_MESSAGE = "Supplier with the Id {%d} was not found.";

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final IndexUtility indexUtility;

    public Long createSupplier(CreateSupplierDto dto) {
        var supplier = supplierMapper.createDtoToEntity(dto);
        var newSupplier = supplierRepository.saveAndFlush(supplier);
        return newSupplier.getId();
    }

    public void updateSupplier(UpdateSupplierDto dto) {

        var supplier = supplierRepository
                .findById(dto.getSupplierId())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, dto.getSupplierId())));

        supplier.setCompanyName(dto.getCompanyName());
        supplier.setContactName(dto.getContactName());
        supplier.setContactPhone(dto.getContactPhone());
        supplier.setAddress(supplierMapper.dtoToAddress(dto.getAddress()));

        supplierRepository.saveAndFlush(supplier);
    }

    public void deleteSupplier(Long supplierId) {

        var supplier = supplierRepository
                .findById(supplierId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, supplierId)));

        supplierRepository.delete(supplier);
    }

    public SupplierDetailsDto getSupplierById(Long supplierId) {
        return supplierRepository
                .findById(supplierId)
                .map(supplierMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, supplierId)));
    }

    private Specification<Supplier> composeSpecification(GetSuppliersRequest request) {

        Specification<Supplier> specification = Specification.where(null);

        if (StringUtils.hasText(request.getCompanyName())) {
            specification = specification.and(SupplierSpecifications.byCompanyNameLike(request.getCompanyName()));
        }
        if (StringUtils.hasText(request.getContactName())) {
            specification = specification.and(SupplierSpecifications.byContactNameLike(request.getContactName()));
        }
        if (StringUtils.hasText(request.getContactPhone())) {
            specification = specification.and(SupplierSpecifications.byContactPhoneLike(request.getContactPhone()));
        }

        var orderByField = !StringUtils.hasText(request.getOrderBy())? "id" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return SupplierSpecifications.orderBy(
                specification,
                orderByField,
                isAscending
        );
    }


    public List<SupplierDto> getSuppliers(GetSuppliersRequest request) {

        var specification = composeSpecification(request);

        return supplierRepository
                .findAll(specification)
                .stream()
                .map(supplierMapper::entityToDto)
                .toList();
    }

    public PagedList<SupplierDto> getSuppliersPaged(GetSuppliersRequest request) {

        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);

        var page = supplierRepository.findAll(specification, pageRequest);
        return supplierMapper.pageToPagedList(page);
    }
}
