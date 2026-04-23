package com.jcanseco.inventoryapi.catalog.products.dto;

import com.jcanseco.inventoryapi.catalog.products.validation.ValidProductPrices;
import jakarta.validation.constraints.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidProductPrices
public class UpdateProductDto extends ProductPricesDto {

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
    @NotBlank
    @Size(max = 50)
    private String name;

    @DecimalMin(value = "0.01")
    @NotNull
    private Double purchasePrice;

    @DecimalMin(value = "0.01")
    @NotNull
    private Double salePrice;
}








