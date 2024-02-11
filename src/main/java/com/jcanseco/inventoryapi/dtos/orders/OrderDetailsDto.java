package com.jcanseco.inventoryapi.dtos.orders;

import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import lombok.*;
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
    private Double total;
    private List<OrderItemDto> items;
    private boolean delivered;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
}
