package com.jcanseco.inventoryapi.dtos.purchases;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    private Long id;
    private String supplier;
    private BigDecimal total;
    private LocalDateTime createdAt;
}