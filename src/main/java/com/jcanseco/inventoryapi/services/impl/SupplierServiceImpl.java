package com.jcanseco.inventoryapi.services.impl;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.GetSuppliersRequest;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.SupplierMapper;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.services.SupplierService;
import com.jcanseco.inventoryapi.specifications.SupplierSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repository;
    private final SupplierMapper mapper;
    private final SupplierSpecifications specifications;

    @Override
    public SupplierDto createSupplier(CreateSupplierDto dto) {
        var supplier = mapper.createDtoToEntity(dto);
        return mapper.entityToDto(supplier);
    }

    @Override
    public SupplierDto updateSupplier(UpdateSupplierDto dto) {

        var supplier = repository
                .findById(dto.getSupplierId())
                .orElseThrow(() -> new NotFoundException(String.format("Supplier with the Id {%d} was not found.", dto.getSupplierId())));

        supplier.setCompanyName(dto.getCompanyName());
        supplier.setContactName(dto.getContactName());
        supplier.setContactPhone(dto.getContactPhone());
        supplier.setAddress(mapper.dtoToAddress(dto.getAddress()));

        var updatedSupplier = repository.saveAndFlush(supplier);

        return mapper.entityToDto(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long supplierId) {

        var supplier = repository
                .findById(supplierId)
                .orElseThrow(() -> new NotFoundException(String.format("Supplier with the Id {%d} was not found.", supplierId)));

        repository.delete(supplier);
    }

    @Override
    public SupplierDto getSupplierById(Long supplierId) {
        return repository
                .findById(supplierId)
                .map(mapper::entityToDto)
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

    @Override
    public List<SupplierDto> getSuppliers(GetSuppliersRequest request) {

        var sort = getSortOrder(request);

        var specification = specifications.getSupplierSpecification(
                request.getCompanyName(),
                request.getContactName(),
                request.getContactPhone()
        );

        if (specification == null) {
            return repository.findAll(sort)
                    .stream()
                    .map(mapper::entityToDto)
                    .toList();
        }

        return repository
                .findAll(specification, sort)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    public PagedList<SupplierDto> getSuppliersPaged(GetSuppliersRequest request) {

        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();

        var specification = specifications.getSupplierSpecification(
                request.getCompanyName(),
                request.getContactName(),
                request.getContactPhone()
        );

        var sort = getSortOrder(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        var page = specification == null? repository.findAll(pageRequest) : repository.findAll(specification, pageRequest);

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
