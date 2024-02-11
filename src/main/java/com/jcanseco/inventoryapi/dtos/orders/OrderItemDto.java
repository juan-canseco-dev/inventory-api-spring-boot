package com.jcanseco.inventoryapi.dtos.orders;

import lombok.*;

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
    private Double price;
    private Double total;
}
