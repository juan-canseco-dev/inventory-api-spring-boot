package com.jcanseco.inventoryapi.dtos.products;

import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDto {
    private Long id;
    private String name;
    private SupplierDto supplier;
    private CategoryDto category;
    private UnitOfMeasurementDto unit;
    private Long stock;
    private Double purchasePrice;
    private Double salePrice;
}