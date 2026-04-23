package com.jcanseco.inventoryapi.orders.dto;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @Min(1)
    @NotNull
    private Long customerId;

    @AllProductsExist
    @PositiveProductQuantities
    @Size(min = 1)
    @NotEmpty
    private HashMap<Long, Long> productsWithQuantities;
}








