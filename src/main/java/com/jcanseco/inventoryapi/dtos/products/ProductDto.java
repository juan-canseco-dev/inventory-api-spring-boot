package com.jcanseco.inventoryapi.dtos.products;

import lombok.*;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String supplier;
    private String category;
    private String unit;
    private Long quantity;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
}
