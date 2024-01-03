package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.*;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.SupplierMapper;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public Long createSupplier(CreateSupplierDto dto) {
        var supplier = supplierMapper.createDtoToEntity(dto);
        var newSupplier = supplierRepository.saveAndFlush(supplier);
        return newSupplier.getId();
    }

    public void updateSupplier(UpdateSupplierDto dto) {

        var supplier = supplierRepository
                .findById(dto.getSupplierId())
                .orElseThrow(() -> new NotFoundException(String.format("Supplier with the Id {%d} was not found.", dto.getSupplierId())));

        supplier.setCompanyName(dto.getCompanyName());
        supplier.setContactName(dto.getContactName());
        supplier.setContactPhone(dto.getContactPhone());
        supplier.setAddress(supplierMapper.dtoToAddress(dto.getAddress()));

        supplierRepository.saveAndFlush(supplier);
    }

    public void deleteSupplier(Long supplierId) {

        var supplier = supplierRepository
                .findById(supplierId)
                .orElseThrow(() -> new NotFoundException(String.format("Supplier with the Id {%d} was not found.", supplierId)));

        supplierRepository.delete(supplier);
    }

    public SupplierDetailsDto getSupplierById(Long supplierId) {
        return supplierRepository
                .findById(supplierId)
                .map(supplierMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Supplier with the Id {%d} was not found.", supplierId)));
    }

    private boolean sortOrderIsAscending(GetSuppliersRequest request) {
        if (request.getSortOrder() == null) {
            return true;
        }
        return request.getSortOrder().equals("asc") || request.getSortOrder().equals("ascending");
    }

    private String getOrderBy(GetSuppliersRequest request) {
        if (request.getOrderBy() == null) {
            return "companyName";
        }
        return request.getOrderBy();
    }

    private Sort getSortOrder(GetSuppliersRequest request) {
        var ascending = sortOrderIsAscending(request);
        var orderBy = getOrderBy(request);
        if (ascending) {
            return Sort.by(orderBy).ascending();
        }
        return Sort.by(orderBy).descending();
    }

    public List<SupplierDto> getSuppliers(GetSuppliersRequest request) {

        var sort = getSortOrder(request);

        var specification = SupplierRepository.Specs.byCompanyAndContactInfo(
                request.getCompanyName(),
                request.getContactName(),
                request.getContactPhone()
        );

        if (specification == null) {
            return supplierRepository.findAll(sort)
                    .stream()
                    .map(supplierMapper::entityToDto)
                    .toList();
        }

        return supplierRepository
                .findAll(specification, sort)
                .stream()
                .map(supplierMapper::entityToDto)
                .toList();
    }

    public PagedList<SupplierDto> getSuppliersPaged(GetSuppliersRequest request) {

        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();

        var specification = SupplierRepository.Specs.byCompanyAndContactInfo(
                request.getCompanyName(),
                request.getContactName(),
                request.getContactPhone()
        );

        var sort = getSortOrder(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        var page = specification == null? supplierRepository.findAll(pageRequest) : supplierRepository.findAll(specification, pageRequest);

        var items = page.get().map(supplierMapper::entityToDto).toList();
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
