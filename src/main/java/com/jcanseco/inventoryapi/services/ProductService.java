package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.products.*;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import static com.jcanseco.inventoryapi.specifications.ProductSpecifications.*;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;
    private final ProductMapper productMapper;
    private final IndexUtility indexUtility;

    @Transactional
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

    @Transactional
    public void deleteProduct(Long productId) {
        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", productId)));

        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductDetailsDto getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .map(productMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", productId)));
    }


    private Specification<Product> orderBySpecification(Specification<Product> spec, GetProductsRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy())? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());
        return switch (orderBy) {
            case "id" -> isAscending? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "name" -> isAscending? orderByNameAsc(spec) : orderByNameDesc(spec);
            case "supplier" -> isAscending? orderBySupplierAsc(spec) : orderBySupplierDesc(spec);
            case "category" -> isAscending? orderByCategoryAsc(spec) : orderByCategoryDesc(spec);
            case "unit" -> isAscending? orderByUnitAsc(spec) : orderByUnitDesc(spec);
            default -> isAscending? orderByNameAsc(spec) : orderByNameDesc(spec);
        };
    }


    private Specification<Product> composeSpecification(GetProductsRequest request) {

        Specification<Product> spec = Specification.where(null);

        if (StringUtils.hasText(request.getName())) {
            spec = spec.and(byNameLike(request.getName()));
        }

        if (request.getSupplierId() != null) {
            var supplier = supplierRepository.findById(request.getSupplierId()).orElse(null);
            spec = spec.and(bySupplier(supplier));
        }

        if (request.getCategoryId() != null) {
            var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            spec = spec.and(byCategory(category));
        }

        if (request.getUnitId() != null) {
            var unit = unitRepository.findById(request.getUnitId()).orElse(null);
            spec = spec.and(byUnit(unit));
        }

        return orderBySpecification(spec, request);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts(GetProductsRequest request) {
        var spec = composeSpecification(request);
        return productRepository.findAll(spec)
                .stream()
                .map(productMapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<ProductDto> getProductsPaged(GetProductsRequest request) {

        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = productRepository.findAll(specification, pageRequest);

        return productMapper.pageToPagedList(page);
    }
}
