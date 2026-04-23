package com.jcanseco.inventoryapi.orders.api;

import com.jcanseco.inventoryapi.orders.dto.*;
import com.jcanseco.inventoryapi.orders.usecases.create.CreateOrderUseCase;
import com.jcanseco.inventoryapi.orders.usecases.delete.DeleteOrderUseCase;
import com.jcanseco.inventoryapi.orders.usecases.deliver.DeliverOrderUseCase;
import com.jcanseco.inventoryapi.orders.usecases.getall.GetOrdersUseCase;
import com.jcanseco.inventoryapi.orders.usecases.getbyid.GetOrderByIdUseCase;
import com.jcanseco.inventoryapi.orders.usecases.update.UpdateOrderUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestControllerAdvice
@RequestMapping("api/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final DeliverOrderUseCase deliverOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final GetOrdersUseCase getOrdersUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateOrderDto dto) throws URISyntaxException {
        var orderId = createOrderUseCase.execute(dto);
        var location = new URI("/api/orders/" + orderId);
        return ResponseEntity.created(location).body(orderId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Update))")
    @PutMapping("{orderId}")
    public ResponseEntity<Void> update(@PathVariable Long orderId, @RequestBody @Valid UpdateOrderDto dto) {
        if (!dto.getOrderId().equals(orderId)) {
            return ResponseEntity.badRequest().build();
        }
        updateOrderUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Deliver))")
    @PutMapping("deliver")
    public ResponseEntity<Void> deliver(@RequestBody @Valid DeliverOrderDto dto) {
        deliverOrderUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.Delete))")
    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        deleteOrderUseCase.execute(orderId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.View))")
    @GetMapping("{orderId}")
    public ResponseEntity<OrderDetailsDto> getById(@PathVariable Long orderId) {
        var response = getOrderByIdUseCase.execute(orderId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Orders, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetOrdersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getOrdersUseCase.execute(request));
        }
        return ResponseEntity.ok(getOrdersUseCase.executePaged(request));
    }
}
