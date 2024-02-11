package com.jcanseco.inventoryapi.dtos.purchases;

import lombok.*;
import java.math.BigDecimal;
@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productUnit;
    private Long quantity;
    private Double price;
    private Double total;
}
