package com.jcanseco.inventoryapi.dtos.purchases;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashMap;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePurchaseDto {

    @Min(1)
    @NotNull
    private Long supplierId;

    @Size(min = 1)
    @NotEmpty
    private HashMap<Long, Long> productsWithQuantities;
}
