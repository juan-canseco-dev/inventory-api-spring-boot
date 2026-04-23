package com.jcanseco.inventoryapi.orders.usecases.create;

import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.orders.dto.CreateOrderDto;
import com.jcanseco.inventoryapi.orders.domain.Order;
import com.jcanseco.inventoryapi.orders.persistence.OrderRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Long execute(CreateOrderDto dto) {
        var customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new DomainException(String.format("Customer with the Id : {%d} was not found.", dto.getCustomerId())));

        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var savedOrder = orderRepository.saveAndFlush(Order.createNew(customer, products, productsWithQuantities));
        return savedOrder.getId();
    }
}

