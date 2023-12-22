package com.jcanseco.inventoryapi.dtos.purchases;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashMap;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePurchaseDto {
    @Min(1)
    @NotNull
    private Long purchaseId;

    @Size(min = 1)
    @NotEmpty
    private HashMap<Long, Long> productsWithQuantities;
}
