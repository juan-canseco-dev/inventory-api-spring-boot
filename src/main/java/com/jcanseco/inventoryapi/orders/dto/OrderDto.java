package com.jcanseco.inventoryapi.orders.dto;

import java.time.LocalDateTime;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String customer;
    private Double total;
    private boolean delivered;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
}








