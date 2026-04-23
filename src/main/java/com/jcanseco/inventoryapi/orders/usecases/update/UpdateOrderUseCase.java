package com.jcanseco.inventoryapi.orders.usecases.update;

import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.orders.dto.UpdateOrderDto;
import com.jcanseco.inventoryapi.orders.persistence.OrderRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateOrderUseCase {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void execute(UpdateOrderDto dto) {
        var order = orderRepository.findWithDetailsById(dto.getOrderId())
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found.", dto.getOrderId())));

        if (order.isDelivered()) {
            throw new DomainException(String.format("Cannot update the order with ID %d because it has already delivered.", dto.getOrderId()));
        }

        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());
        order.update(products, productsWithQuantities);

        orderRepository.save(order);
    }
}

