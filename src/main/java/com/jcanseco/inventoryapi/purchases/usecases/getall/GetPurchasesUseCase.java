package com.jcanseco.inventoryapi.purchases.usecases.getall;

import com.jcanseco.inventoryapi.purchases.dto.GetPurchasesRequest;
import com.jcanseco.inventoryapi.purchases.dto.PurchaseDto;
import com.jcanseco.inventoryapi.purchases.domain.Purchase;
import com.jcanseco.inventoryapi.purchases.mapping.PurchaseMapper;
import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
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
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.byArrived;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.byArrivedBetween;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.byOrderedBetween;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.bySupplier;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByArrivedAsc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByArrivedAtAsc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByArrivedAtDesc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByArrivedDesc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByIdAsc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByIdDesc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByOrderedAtAsc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByOrderedAtDesc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderBySupplierAsc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderBySupplierDesc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByTotalAsc;
import static com.jcanseco.inventoryapi.purchases.persistence.PurchaseSpecifications.orderByTotalDesc;

@Service
@RequiredArgsConstructor
public class GetPurchasesUseCase {

    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final IndexUtility indexUtility;

    @Transactional(readOnly = true)
    public List<PurchaseDto> execute(GetPurchasesRequest request) {
        var spec = composeSpecification(request);
        return purchaseRepository.findAll(spec)
                .stream()
                .map(purchaseMapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<PurchaseDto> executePaged(GetPurchasesRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = purchaseRepository.findAll(specification, pageRequest);
        return purchaseMapper.pageToPagedList(page);
    }

    private Specification<Purchase> composeSpecification(GetPurchasesRequest request) {
        Specification<Purchase> spec = Specification.where(null);

        if (request.getSupplierId() != null) {
            var supplier = supplierRepository.findById(request.getSupplierId()).orElse(null);
            spec = spec.and(bySupplier(supplier));
        }

        if (request.getArrived() != null) {
            spec = spec.and(byArrived(request.getArrived()));
        }

        if (request.getOrderedAtStartDate() != null && request.getOrderedAtEndDate() != null) {
            spec = spec.and(byOrderedBetween(request.getOrderedAtStartDate(), request.getOrderedAtEndDate()));
        }

        if (request.getArrivedAtStartDate() != null && request.getArrivedAtEndDate() != null) {
            spec = spec.and(byArrivedBetween(request.getArrivedAtStartDate(), request.getArrivedAtEndDate()));
        }

        return orderBySpecification(spec, request);
    }

    private Specification<Purchase> orderBySpecification(Specification<Purchase> spec, GetPurchasesRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy()) ? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return switch (orderBy) {
            case "id" -> isAscending ? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "supplier" -> isAscending ? orderBySupplierAsc(spec) : orderBySupplierDesc(spec);
            case "total" -> isAscending ? orderByTotalAsc(spec) : orderByTotalDesc(spec);
            case "arrived" -> isAscending ? orderByArrivedAsc(spec) : orderByArrivedDesc(spec);
            case "arrivedAt" -> isAscending ? orderByArrivedAtAsc(spec) : orderByArrivedAtDesc(spec);
            case "orderedAt" -> isAscending ? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
            default -> isAscending ? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
        };
    }
}

