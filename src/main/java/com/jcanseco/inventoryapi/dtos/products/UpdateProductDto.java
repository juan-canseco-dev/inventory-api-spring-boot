package com.jcanseco.inventoryapi.dtos.products;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDto {

    @Min(1)
    @NotNull
    private Long productId;

    @Min(1)
    @NotNull
    private Long supplierId;

    @Min(1)
    @NotNull
    private Long categoryId;

    @Min(1)
    @NotNull
    private Long unitId;

    @NotEmpty
    @NotNull
    private String name;

    @NotNull
    private Double purchasePrice;

    @NotNull
    private Double salePrice;
}
