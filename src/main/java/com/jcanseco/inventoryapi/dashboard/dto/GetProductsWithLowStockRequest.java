package com.jcanseco.inventoryapi.dashboard.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsWithLowStockRequest {

    @Min(0)
    Long stockThreshold;

    @Min(1)
    int limit;
}
