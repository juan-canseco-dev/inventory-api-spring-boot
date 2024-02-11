package com.jcanseco.inventoryapi.dtos.products;

import lombok.*;

@EqualsAndHashCode
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
    private Long stock;
    private Double purchasePrice;
    private Double salePrice;
}
