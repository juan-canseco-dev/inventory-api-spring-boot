package com.jcanseco.inventoryapi.dtos.orders;

import com.jcanseco.inventoryapi.validators.allproductsexist.AllProductsExist;
import com.jcanseco.inventoryapi.validators.positiveproductquantities.PositiveProductQuantities;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDto {

    @Min(1)
    @NotNull
    private Long customerId;

    @AllProductsExist
    @PositiveProductQuantities
    @Size(min =  1)
    @NotEmpty
    private HashMap<Long, Long> productsWithQuantities;
}
