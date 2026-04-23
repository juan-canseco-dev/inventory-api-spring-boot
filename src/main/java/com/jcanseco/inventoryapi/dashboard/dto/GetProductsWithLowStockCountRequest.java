package com.jcanseco.inventoryapi.dashboard.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsWithLowStockCountRequest {
    @Min(0)
    Long stockThreshold;
}
