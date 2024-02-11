package com.jcanseco.inventoryapi.dtos.purchases;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    private Long id;
    private String supplier;
    private Double total;
    private boolean arrived;
    private LocalDateTime orderedAt;
    private LocalDateTime arrivedAt;
}