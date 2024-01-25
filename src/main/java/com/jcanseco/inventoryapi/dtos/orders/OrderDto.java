package com.jcanseco.inventoryapi.dtos.orders;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String customer;
    private BigDecimal total;
    private boolean delivered;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
}
