package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.orders.CreateOrderDto;
import com.jcanseco.inventoryapi.dtos.orders.GetOrdersRequest;
import com.jcanseco.inventoryapi.dtos.orders.OrderDetailsDto;
import com.jcanseco.inventoryapi.dtos.orders.UpdateOrderDto;
import com.jcanseco.inventoryapi.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@RestControllerAdvice
@RequestMapping("api/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateOrderDto dto) throws URISyntaxException {
        var orderId = orderService.createOrder(dto);
        var location = new URI("/api/orders/" + orderId);
        return ResponseEntity.created(location).body(orderId);
    }

    @PutMapping("{orderId}")
    public ResponseEntity<Void> update(@PathVariable Long orderId, @RequestBody @Valid UpdateOrderDto dto) {
        if (!dto.getOrderId().equals(orderId)) {
            return ResponseEntity.badRequest().build();
        }
        orderService.updateOrder(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{orderId}/deliver")
    public ResponseEntity<Void> deliver(@PathVariable Long orderId) {
        orderService.deliverOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDetailsDto> getById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetOrdersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(orderService.getOrders(request));
        }
        return ResponseEntity.ok(orderService.getOrdersPage(request));
    }
}
