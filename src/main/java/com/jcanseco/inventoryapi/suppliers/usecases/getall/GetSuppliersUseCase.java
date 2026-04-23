package com.jcanseco.inventoryapi.suppliers.usecases.getall;

import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.shared.utils.IndexUtility;
import com.jcanseco.inventoryapi.suppliers.dto.GetSuppliersRequest;
import com.jcanseco.inventoryapi.suppliers.dto.SupplierDto;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import com.jcanseco.inventoryapi.suppliers.mapping.SupplierMapper;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierSpecifications;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GetSuppliersUseCase {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final IndexUtility indexUtility;

    public List<SupplierDto> execute(GetSuppliersRequest request) {
        var specification = composeSpecification(request);
        return supplierRepository.findAll(specification)
                .stream()
                .map(supplierMapper::entityToDto)
                .toList();
    }

    public PagedList<SupplierDto> executePaged(GetSuppliersRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = supplierRepository.findAll(specification, pageRequest);
        return supplierMapper.pageToPagedList(page);
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

        var orderByField = !StringUtils.hasText(request.getOrderBy()) ? "id" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return SupplierSpecifications.orderBy(specification, orderByField, isAscending);
    }
}

