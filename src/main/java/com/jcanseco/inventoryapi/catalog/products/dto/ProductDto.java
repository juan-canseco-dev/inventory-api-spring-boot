package com.jcanseco.inventoryapi.catalog.products.dto;

import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



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








