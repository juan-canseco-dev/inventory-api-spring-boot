package com.jcanseco.inventoryapi.dtos.products;

import com.jcanseco.inventoryapi.validators.productprice.ProductPrice;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ProductPrice(
        purchasePrice = "purchasePrice",
        salePrice = "salePrice"
)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {
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
