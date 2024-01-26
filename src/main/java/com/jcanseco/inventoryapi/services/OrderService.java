package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.orders.*;
import com.jcanseco.inventoryapi.entities.Order;
import com.jcanseco.inventoryapi.entities.OrderItem;
import com.jcanseco.inventoryapi.entities.Stock;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.OrderMapper;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import com.jcanseco.inventoryapi.repositories.OrderRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.StockRepository;
import com.jcanseco.inventoryapi.specifications.StockSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;
import static com.jcanseco.inventoryapi.specifications.OrderSpecifications.*;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IndexUtility indexUtility;

    @Transactional
    public Long createOrder(CreateOrderDto dto) {
        var customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new DomainException(String.format("Customer with the Id : {%d} was not found.", dto.getCustomerId())));

        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var savedOrder = orderRepository.saveAndFlush(
                Order.createNew(customer, products, productsWithQuantities)
        );

        return savedOrder.getId();
    }

    @Transactional
    public void deliverOrder(Long orderId) {

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found.", orderId)));

        order.deliver();
        var productsWithQuantities = order.getItems().stream()
                .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

        var stocks = stockRepository.findAll(
                StockSpecifications.byProductIds(productsWithQuantities.keySet().stream().toList())
        );

        stocks.forEach(s -> s.removeStock(
                productsWithQuantities.get(s.getProductId())
        ));

        orderRepository.saveAndFlush(order);
        stockRepository.saveAllAndFlush(stocks);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found.", orderId)));

        if (order.isDelivered()) {
            throw new DomainException(String.format("Cannot delete the order with ID %d because it has already delivered.", orderId));
        }

        orderRepository.delete(order);
    }

    @Transactional
    public void updateOrder(UpdateOrderDto dto) {

        var order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found.", dto.getOrderId())));

        if (order.isDelivered()) {
            throw new DomainException(String.format("Cannot update the order with ID %d because it has already delivered.", dto.getOrderId()));
        }

        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());
        order.update(products, productsWithQuantities);

        orderRepository.saveAndFlush(order);
    }

    @Transactional(readOnly = true)
    public OrderDetailsDto getOrderById(Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(orderMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found", orderId)));
    }

    private Specification<Order> orderBySpecification(Specification<Order> spec, GetOrdersRequest request) {

        var orderBy = !StringUtils.hasText(request.getOrderBy())? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return switch (orderBy) {
            case "id" -> isAscending? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "customer" -> isAscending? orderByCustomerAsc(spec) : orderByCustomerDesc(spec);
            case "total" -> isAscending? orderByTotalAsc(spec) : orderByTotalDesc(spec);
            case "delivered" -> isAscending? orderByDeliveredAsc(spec) : orderByDeliveredDesc(spec);
            case "deliveredAt" -> isAscending? orderByDeliveredAtAsc(spec) : orderByDeliveredAtDesc(spec);
            case "orderedAt" -> isAscending? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
            default -> isAscending? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
        };
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

    public List<OrderDto> getOrders(GetOrdersRequest request) {
        var spec = composeSpecification(request);
        return orderRepository.findAll(spec)
                .stream()
                .map(orderMapper::entityToDto)
                .toList();
    }

    public PagedList<OrderDto> getOrdersPage(GetOrdersRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = orderRepository.findAll(specification, pageRequest);

        return orderMapper.pageToPagedList(page);
    }
}
