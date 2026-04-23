package com.jcanseco.inventoryapi.customers.usecases.getall;

import com.jcanseco.inventoryapi.customers.dto.CustomerDto;
import com.jcanseco.inventoryapi.customers.dto.GetCustomersRequest;
import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.customers.mapping.CustomerMapper;
import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.customers.persistence.CustomerSpecifications;
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
public class GetCustomersUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final IndexUtility indexUtility;

    public List<CustomerDto> execute(GetCustomersRequest request) {
        var specification = composeSpecification(request);
        return customerRepository.findAll(specification)
                .stream()
                .map(customerMapper::entityToDto)
                .toList();
    }

    public PagedList<CustomerDto> executePaged(GetCustomersRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = customerRepository.findAll(specification, pageRequest);
        return customerMapper.pageToPagedList(page);
    }

    private Specification<Customer> composeSpecification(GetCustomersRequest request) {
        Specification<Customer> specification = Specification.where(null);

        if (StringUtils.hasText(request.getDni())) {
            specification = specification.and(CustomerSpecifications.byDniLike(request.getDni()));
        }
        if (StringUtils.hasText(request.getPhone())) {
            specification = specification.and(CustomerSpecifications.byPhoneLike(request.getPhone()));
        }
        if (StringUtils.hasText(request.getFullName())) {
            specification = specification.and(CustomerSpecifications.byFullNameLike(request.getFullName()));
        }

        var isAscendingOrder = indexUtility.isAscendingOrder(request.getSortOrder());
        var orderByField = !StringUtils.hasText(request.getOrderBy()) ? "id" : request.getOrderBy();

        return CustomerSpecifications.orderBy(specification, orderByField, isAscendingOrder);
    }
}

