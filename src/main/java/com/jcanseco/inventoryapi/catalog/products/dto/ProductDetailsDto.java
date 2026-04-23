package com.jcanseco.inventoryapi.catalog.products.dto;

import com.jcanseco.inventoryapi.catalog.categories.dto.CategoryDto;
import com.jcanseco.inventoryapi.catalog.units.dto.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.suppliers.dto.SupplierDto;
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







