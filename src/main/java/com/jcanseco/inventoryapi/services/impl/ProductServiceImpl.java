package com.jcanseco.inventoryapi.services.impl;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.products.*;
import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;
    private final ProductMapper mapper;

    @Transactional
    @Override
    public Long createProduct(CreateProductDto dto) {

        var supplier = supplierRepository
                .findById(dto.getSupplierId())
                .orElseThrow(() -> new DomainException(String.format("The Supplier with the Id {%d} was not found.", dto.getSupplierId())));

        var category = categoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new DomainException(String.format("The Category with the Id {%d} was not found.", dto.getCategoryId())));

        var unit = unitRepository
                .findById(dto.getUnitId())
                .orElseThrow(() -> new DomainException(String.format("The Unit Of Measurement with the Id {%d} was not found.", dto.getUnitId())));


        if (dto.getSalePrice() < dto.getPurchasePrice()) {
            throw new DomainException("The Sale Price must be greater than Purchase Price.");
        }


        var product = Product.builder()
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(dto.getName())
                .quantity(0L)
                .purchasePrice(BigDecimal.valueOf(dto.getPurchasePrice()))
                .salePrice(BigDecimal.valueOf(dto.getSalePrice()))
                .build();

        var newProduct = productRepository.saveAndFlush(product);

        return newProduct.getId();
    }

    @Transactional
    @Override
    public void updateProduct(UpdateProductDto dto) {

        var product = productRepository
                .findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", dto.getProductId())));

        if (!product.getSupplier().getId().equals(dto.getSupplierId())) {
            var supplier = supplierRepository
                    .findById(dto.getSupplierId())
                    .orElseThrow(() -> new DomainException(String.format("The Supplier with the Id {%d} was not found.", dto.getSupplierId())));
            product.setSupplier(supplier);
        }

        if (!product.getCategory().getId().equals(dto.getCategoryId())) {
            var category = categoryRepository
                    .findById(dto.getCategoryId())
                    .orElseThrow(() -> new DomainException(String.format("The Category with the Id {%d} was not found.", dto.getCategoryId())));
            product.setCategory(category);
        }

        if (!product.getUnit().getId().equals(dto.getUnitId())) {
            var unit = unitRepository
                    .findById(dto.getUnitId())
                    .orElseThrow(() -> new DomainException(String.format("The Unit Of Measurement with the Id {%d} was not found.", dto.getUnitId())));
            product.setUnit(unit);
        }

        if (dto.getSalePrice() < dto.getPurchasePrice()) {
            throw new DomainException("The Sale Price must be greater than Purchase Price.");
        }

        product.setName(dto.getName());

        product.setPurchasePrice(BigDecimal.valueOf(dto.getPurchasePrice()));
        product.setSalePrice(BigDecimal.valueOf(dto.getSalePrice()));

        productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", productId)));

        productRepository.delete(product);
    }

    @Override
    public ProductDetailsDto getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .map(mapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", productId)));
    }


    private boolean sortOrderIsAscending(GetProductsRequest request) {
        if (request.getSortOrder() == null) {
            return true;
        }
        return request.getSortOrder().equals("asc") || request.getSortOrder().equals("ascending");
    }

    private String getOrderBy(GetProductsRequest request) {
        if (request.getOrderBy() == null) {
            return "name";
        }
        return request.getOrderBy();
    }

    private Sort getSortOrder(GetProductsRequest request) {
        var ascending = sortOrderIsAscending(request);
        var orderBy = getOrderBy(request);
        if (ascending) {
            return Sort.by(orderBy).ascending();
        }
        return Sort.by(orderBy).descending();
    }

    @Override
    public List<ProductDto> getProducts(GetProductsRequest request) {

        Supplier supplier = null;
        Category category = null;
        UnitOfMeasurement unit = null;

        if (request.getSupplierId() != null) {
            supplier = supplierRepository
                    .findById(request.getSupplierId())
                    .orElse(null);
        }

        if (request.getCategoryId() != null) {
            category = categoryRepository
                    .findById(request.getCategoryId())
                    .orElse(null);
        }

        if (request.getUnitId() != null) {
            unit = unitRepository
                    .findById(request.getUnitId())
                    .orElse(null);
        }

        var sort = getSortOrder(request);
        var specification = ProductRepository.Specs.byAllFilters(
                supplier,
                category,
                unit,
                request.getName()
        );

        if (specification == null) {
            return productRepository.findAll(sort)
                    .stream()
                    .map(mapper::entityToDto)
                    .toList();
        }

        return productRepository
                .findAll(specification, sort)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    public PagedList<ProductDto> getProductsPaged(GetProductsRequest request) {

        var pageNumber = request.getPageNumber() > 0? request.getPageNumber() - 1 : request.getPageNumber();
        var pageSize = request.getPageSize();

        Supplier supplier = null;
        Category category = null;
        UnitOfMeasurement unit = null;

        if (request.getSupplierId() != null) {
            supplier = supplierRepository
                    .findById(request.getSupplierId())
                    .orElse(null);
        }

        if (request.getCategoryId() != null) {
            category = categoryRepository
                    .findById(request.getCategoryId())
                    .orElse(null);
        }

        if (request.getUnitId() != null) {
            unit = unitRepository
                    .findById(request.getUnitId())
                    .orElse(null);
        }

        var sort = getSortOrder(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        var specification = ProductRepository.Specs.byAllFilters(
                supplier,
                category,
                unit,
                request.getName()
        );

        var page = specification == null? productRepository.findAll(pageRequest) : productRepository.findAll(specification, pageRequest);

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
