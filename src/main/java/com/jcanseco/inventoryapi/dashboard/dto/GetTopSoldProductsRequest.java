package com.jcanseco.inventoryapi.dashboard.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetTopSoldProductsRequest {
    @Min(1)
    private int limit;
}
