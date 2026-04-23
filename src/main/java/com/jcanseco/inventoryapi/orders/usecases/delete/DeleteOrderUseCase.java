package com.jcanseco.inventoryapi.orders.usecases.delete;

import com.jcanseco.inventoryapi.orders.persistence.OrderRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteOrderUseCase {

    private final OrderRepository orderRepository;

    @Transactional
    public void execute(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found.", orderId)));

        if (order.isDelivered()) {
            throw new DomainException(String.format("Cannot delete the order with ID %d because it has already delivered.", orderId));
        }

        orderRepository.delete(order);
    }
}

