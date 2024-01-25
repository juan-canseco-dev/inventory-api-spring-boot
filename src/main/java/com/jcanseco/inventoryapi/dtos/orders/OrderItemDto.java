package com.jcanseco.inventoryapi.dtos.orders;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productUnit;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal total;
}
