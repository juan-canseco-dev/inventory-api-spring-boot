package com.jcanseco.inventoryapi.dtos.purchases;

import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import lombok.*;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    private Long id;
    private SupplierDto supplier;
    private BigDecimal total;
}