package com.jcanseco.inventoryapi.orders.usecases.getbyid;

import com.jcanseco.inventoryapi.orders.dto.OrderDetailsDto;
import com.jcanseco.inventoryapi.orders.mapping.OrderMapper;
import com.jcanseco.inventoryapi.orders.persistence.OrderRepository;
import com.jcanseco.inventoryapi.orders.persistence.OrderSpecifications;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetOrderByIdUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public OrderDetailsDto execute(Long orderId) {
        return orderRepository.findWithDetailsById(orderId)
                .map(orderMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found", orderId)));
    }
}

