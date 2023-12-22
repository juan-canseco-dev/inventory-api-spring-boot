package com.jcanseco.inventoryapi.dtos.purchases;

import lombok.*;
import java.math.BigDecimal;
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemDto {
    private Long id;
    private String productName;
    private String productUnit;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal total;
}
