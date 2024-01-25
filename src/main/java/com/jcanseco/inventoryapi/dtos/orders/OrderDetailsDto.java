package com.jcanseco.inventoryapi.dtos.orders;

import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseItemDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {
    private Long id;
    private CustomerDto customer;
    private BigDecimal total;
    private List<OrderItemDto> items;
    private boolean delivered;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
}
