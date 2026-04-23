package com.jcanseco.inventoryapi.purchases.dto;

import com.jcanseco.inventoryapi.inventory.validation.AllProductsExist;
import com.jcanseco.inventoryapi.inventory.validation.positivequantities.PositiveProductQuantities;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import lombok.*;





@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePurchaseDto {

    @Min(1)
    @NotNull
    private Long purchaseId;

    @AllProductsExist
    @PositiveProductQuantities
    @Size(min = 1)
    @NotEmpty
    private HashMap<Long, Long> productsWithQuantities;
}








