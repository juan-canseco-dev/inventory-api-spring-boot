package com.jcanseco.inventoryapi.dtos.orders;

import lombok.*;
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
    private Double total;
    private boolean delivered;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
}
