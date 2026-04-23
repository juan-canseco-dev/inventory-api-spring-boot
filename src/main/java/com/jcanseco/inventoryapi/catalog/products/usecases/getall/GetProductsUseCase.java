package com.jcanseco.inventoryapi.catalog.products.usecases.getall;

import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.catalog.products.dto.GetProductsRequest;
import com.jcanseco.inventoryapi.catalog.products.dto.ProductDto;
import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.products.mapping.ProductMapper;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.shared.utils.IndexUtility;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.byCategory;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.byNameLike;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.bySupplier;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.byUnit;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByCategoryAsc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByCategoryDesc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByIdAsc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByIdDesc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByNameAsc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByNameDesc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByStockAsc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByStockDesc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderBySupplierAsc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderBySupplierDesc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByUnitAsc;
import static com.jcanseco.inventoryapi.catalog.products.persistence.ProductSpecifications.orderByUnitDesc;

@Service
@RequiredArgsConstructor
public class GetProductsUseCase {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;
    private final ProductMapper productMapper;
    private final IndexUtility indexUtility;

    @Transactional(readOnly = true)
    public List<ProductDto> execute(GetProductsRequest request) {
        var spec = composeSpecification(request);
        return productRepository.findAll(spec)
                .stream()
                .map(productMapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<ProductDto> executePaged(GetProductsRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = productRepository.findAll(specification, pageRequest);
        return productMapper.pageToPagedList(page);
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

    private Specification<Product> orderBySpecification(Specification<Product> spec, GetProductsRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy()) ? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());
        return switch (orderBy) {
            case "id" -> isAscending ? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "name" -> isAscending ? orderByNameAsc(spec) : orderByNameDesc(spec);
            case "supplier" -> isAscending ? orderBySupplierAsc(spec) : orderBySupplierDesc(spec);
            case "category" -> isAscending ? orderByCategoryAsc(spec) : orderByCategoryDesc(spec);
            case "unit" -> isAscending ? orderByUnitAsc(spec) : orderByUnitDesc(spec);
            case "stock" -> isAscending ? orderByStockAsc(spec) : orderByStockDesc(spec);
            default -> isAscending ? orderByNameAsc(spec) : orderByNameDesc(spec);
        };
    }
}

