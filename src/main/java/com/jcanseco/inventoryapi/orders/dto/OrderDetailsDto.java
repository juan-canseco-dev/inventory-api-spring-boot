package com.jcanseco.inventoryapi.orders.dto;

import com.jcanseco.inventoryapi.customers.dto.CustomerDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

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
    private String deliverComments;
}








