package com.jcanseco.inventoryapi.dtos.products;

import jakarta.validation.constraints.*;
import lombok.*;

@EqualsAndHashCode
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
