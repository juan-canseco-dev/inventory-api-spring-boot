package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.orders.CreateOrderDto;
import com.jcanseco.inventoryapi.dtos.orders.GetOrdersRequest;
import com.jcanseco.inventoryapi.dtos.orders.OrderDetailsDto;
import com.jcanseco.inventoryapi.dtos.orders.UpdateOrderDto;
import com.jcanseco.inventoryapi.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateOrderDto dto) throws URISyntaxException {
        var orderId = orderService.createOrder(dto);
        var location = new URI("/api/orders/" + orderId);
        return ResponseEntity.created(location).body(orderId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Update))")
    @PutMapping("{orderId}")
    public ResponseEntity<Void> update(@PathVariable Long orderId, @RequestBody @Valid UpdateOrderDto dto) {
        if (!dto.getOrderId().equals(orderId)) {
            return ResponseEntity.badRequest().build();
        }
        orderService.updateOrder(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Deliver))")
    @PutMapping("{orderId}/deliver")
    public ResponseEntity<Void> deliver(@PathVariable Long orderId) {
        orderService.deliverOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Delete))")
    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.View))")
    @GetMapping("{orderId}")
    public ResponseEntity<OrderDetailsDto> getById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetOrdersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(orderService.getOrders(request));
        }
        return ResponseEntity.ok(orderService.getOrdersPage(request));
    }
}
