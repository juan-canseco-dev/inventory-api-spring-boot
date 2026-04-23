package com.jcanseco.inventoryapi.purchases.dto;

import java.math.BigDecimal;
import lombok.*;




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








