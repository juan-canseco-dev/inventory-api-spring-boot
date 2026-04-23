package com.jcanseco.inventoryapi.orders.usecases.getall;

import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.orders.dto.GetOrdersRequest;
import com.jcanseco.inventoryapi.orders.dto.OrderDto;
import com.jcanseco.inventoryapi.orders.domain.Order;
import com.jcanseco.inventoryapi.orders.mapping.OrderMapper;
import com.jcanseco.inventoryapi.orders.persistence.OrderRepository;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.shared.utils.IndexUtility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.byCustomer;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.byDelivered;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.byDeliveredBetween;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.byOrderedBetween;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByCustomerAsc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByCustomerDesc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByDeliveredAsc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByDeliveredAtAsc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByDeliveredAtDesc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByDeliveredDesc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByIdAsc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByIdDesc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByOrderedAtAsc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByOrderedAtDesc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByTotalAsc;
import static com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications.orderByTotalDesc;

@Service
@RequiredArgsConstructor
public class GetOrdersUseCase {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IndexUtility indexUtility;

    @Transactional(readOnly = true)
    public List<OrderDto> execute(GetOrdersRequest request) {
        var spec = composeSpecification(request);
        return orderRepository.findAll(spec)
                .stream()
                .map(orderMapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<OrderDto> executePaged(GetOrdersRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = orderRepository.findAll(specification, pageRequest);
        return orderMapper.pageToPagedList(page);
    }

    private Specification<Order> composeSpecification(GetOrdersRequest request) {
        Specification<Order> spec = Specification.where(null);

        if (request.getCustomerId() != null) {
            var customer = customerRepository.findById(request.getCustomerId()).orElse(null);
            spec = spec.and(byCustomer(customer));
        }

        if (request.getDelivered() != null) {
            spec = spec.and(byDelivered(request.getDelivered()));
        }

        if (request.getOrderedAtStartDate() != null && request.getOrderedAtEndDate() != null) {
            spec = spec.and(byOrderedBetween(request.getOrderedAtStartDate(), request.getOrderedAtEndDate()));
        }

        if (request.getDeliveredAtStartDate() != null && request.getDeliveredAtEndDate() != null) {
            spec = spec.and(byDeliveredBetween(request.getDeliveredAtStartDate(), request.getDeliveredAtEndDate()));
        }

        return orderBySpecification(spec, request);
    }

    private Specification<Order> orderBySpecification(Specification<Order> spec, GetOrdersRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy()) ? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return switch (orderBy) {
            case "id" -> isAscending ? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "customer" -> isAscending ? orderByCustomerAsc(spec) : orderByCustomerDesc(spec);
            case "total" -> isAscending ? orderByTotalAsc(spec) : orderByTotalDesc(spec);
            case "delivered" -> isAscending ? orderByDeliveredAsc(spec) : orderByDeliveredDesc(spec);
            case "deliveredAt" -> isAscending ? orderByDeliveredAtAsc(spec) : orderByDeliveredAtDesc(spec);
            case "orderedAt" -> isAscending ? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
            default -> isAscending ? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
        };
    }
}

